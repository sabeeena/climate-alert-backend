package kz.geowarning.notification.service;

import kz.geowarning.notification.dto.NotificationDTO;
import kz.geowarning.notification.dto.ReportNotificationDTO;
import kz.geowarning.notification.entity.AlertNotification;
import kz.geowarning.notification.entity.ReportNotification;
import kz.geowarning.notification.repository.AlertNotificationRepository;
import kz.geowarning.notification.repository.ReportNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ManageNotificationService {
    @Autowired
    private AlertNotificationRepository alertNotificationRepository;

    @Autowired
    private ReportNotificationRepository reportNotificationRepository;

    public void deleteAlertNotification(Long id) {
        alertNotificationRepository.deleteById(id);
    }

    public void deleteReportNotification(Long id) {
        reportNotificationRepository.deleteById(id);
    }

    public void markAlertAsSeen(Long id) throws RuntimeException {
        AlertNotification alertNotification = alertNotificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert Notification not found with id: " + id));

        alertNotification.setSeen(true);
        alertNotificationRepository.save(alertNotification);
    }

    public void markReportAsSeen(Long id) throws RuntimeException {
        ReportNotification reportNotification = reportNotificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report Notification not found with id: " + id));

        reportNotification.setSeen(true);
        reportNotificationRepository.save(reportNotification);
    }

    public List<AlertNotification> getAllAlertNotifications(String email) {
        return alertNotificationRepository.findByReceiverEmail(email);
    }

    public List<ReportNotification> getAllReportNotifications() {
        return reportNotificationRepository.findAll();
    }

    public List<NotificationDTO> getAllNotifications(String email, String language) {
        List<NotificationDTO> notifications = new ArrayList<>();

        List<AlertNotification> alertNotifications = alertNotificationRepository.findByReceiverEmail(email);
        List<ReportNotification> reportNotifications = reportNotificationRepository.findByReceiverEmail(email);

        // Convert AlertNotification to NotificationDTO
        alertNotifications.forEach(alert -> {
            NotificationDTO dto = new NotificationDTO();
            dto.setId(alert.getId());
            dto.setReceiverEmail(alert.getReceiverEmail());
            if (language == null || language.isEmpty()) {
                dto.setText(alert.getTextEn());
            } else if (language.equalsIgnoreCase("RU")) {
                dto.setText(alert.getTextRu());
            } else if (language.equalsIgnoreCase("KZ")) {
                dto.setText(alert.getTextKz());
            } else {
                dto.setText(alert.getTextEn());
            }
            dto.setType(alert.getWarningType());
            dto.setRegion(alert.getRegion());
            dto.setDangerPossibility(alert.getDangerPossibility());
            dto.setSeen(alert.isSeen());
            dto.setNotificationType("alert");
            dto.setSentTime(alert.getSentTime());
            notifications.add(dto);
        });

        // Convert ReportNotification to NotificationDTO
        reportNotifications.forEach(report -> {
            NotificationDTO dto = new NotificationDTO();
            dto.setId(report.getId());
            dto.setReceiverEmail(report.getReceiverEmail());
            dto.setSenderEmail(report.getSenderEmail());
            dto.setText(report.getText());
            dto.setTypeStatus(report.getTypeStatus());
            dto.setReportType(report.getReportType());
            dto.setReportId(report.getReportId());
            dto.setSeen(report.isSeen());
            dto.setNotificationType("report");
            dto.setSentTime(report.getSentTime());
            notifications.add(dto);
        });

        Comparator<NotificationDTO> comparator = new Comparator<NotificationDTO>() {
            @Override
            public int compare(NotificationDTO n1, NotificationDTO n2) {
                LocalDateTime time1 = n1.getSentTime();
                LocalDateTime time2 = n2.getSentTime();
                if (time1 == null && time2 == null) {
                    return 0; // Оба времени равны (оба null)
                } else if (time1 == null) {
                    return 1; // n1 после n2, так как time1 null
                } else if (time2 == null) {
                    return -1; // n2 после n1, так как time2 null
                } else {
                    return time2.compareTo(time1);
                }
            }
        };

        Collections.sort(notifications, comparator);

        return notifications;
    }

    public NotificationDTO getReportNotificationById(Long id, String notificationType, String language) throws RuntimeException {
        ReportNotification reportNotification = null;
        AlertNotification alertNotification = null;
        if (Objects.equals(notificationType, "report")){
            reportNotification = reportNotificationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Report Notification not found with id: " + id));
            NotificationDTO dto = new NotificationDTO();
            dto.setId(reportNotification.getId());
            dto.setReceiverEmail(reportNotification.getReceiverEmail());
            dto.setSenderEmail(reportNotification.getSenderEmail());
            dto.setText(reportNotification.getText());
            dto.setTypeStatus(reportNotification.getTypeStatus());
            dto.setReportType(reportNotification.getReportType());
            dto.setReportId(reportNotification.getReportId());
            dto.setSeen(reportNotification.isSeen());
            dto.setSentTime(reportNotification.getSentTime());
            dto.setNotificationType("report");
            return dto;
        } else {
            alertNotification = alertNotificationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Report Notification not found with id: " + id));
            NotificationDTO dto = new NotificationDTO();
            dto.setId(alertNotification.getId());
            dto.setReceiverEmail(alertNotification.getReceiverEmail());
            if (language == null || language.isEmpty()) {
                dto.setText(alertNotification.getTextEn());
            } else if (language.toUpperCase().equals("RU")) {
                dto.setText(alertNotification.getTextRu());
            } else if (language.toUpperCase().equals("KZ")) {
                dto.setText(alertNotification.getTextKz());
            } else {
                dto.setText(alertNotification.getTextEn());
            }
            dto.setType(alertNotification.getWarningType());
            dto.setRegion(alertNotification.getRegion());
            dto.setDangerPossibility(alertNotification.getDangerPossibility());
            dto.setSeen(alertNotification.isSeen());
            dto.setSentTime(alertNotification.getSentTime());
            dto.setNotificationType("alert");
            return dto;
        }
    }
}
