package kz.geowarning.notification.service;

import kz.geowarning.common.exceptions.NotFoundException;
import kz.geowarning.notification.dto.NotificationDTO;
import kz.geowarning.notification.entity.AlertNotification;
import kz.geowarning.notification.entity.ReportNotification;
import kz.geowarning.notification.repository.AlertNotificationRepository;
import kz.geowarning.notification.repository.ReportNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public void markAlertAsSeen(Long id) throws NotFoundException {
        AlertNotification alertNotification = alertNotificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alert Notification not found with id: " + id));

        alertNotification.setSeen(true);
        alertNotificationRepository.save(alertNotification);
    }

    public void markReportAsSeen(Long id) throws NotFoundException {
        ReportNotification reportNotification = reportNotificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Report Notification not found with id: " + id));

        reportNotification.setSeen(true);
        reportNotificationRepository.save(reportNotification);
    }

    public List<AlertNotification> getAllAlertNotifications() {
        return alertNotificationRepository.findAll();
    }

    public List<ReportNotification> getAllReportNotifications() {
        return reportNotificationRepository.findAll();
    }

    public List<NotificationDTO> getAllNotifications(String email) {
        List<NotificationDTO> notifications = new ArrayList<>();

        List<AlertNotification> alertNotifications = alertNotificationRepository.findByReceiverEmail(email);
        List<ReportNotification> reportNotifications = reportNotificationRepository.findByReceiverEmail(email);

        // Convert AlertNotification to NotificationDTO
        alertNotifications.forEach(alert -> {
            NotificationDTO dto = new NotificationDTO();
            dto.setId(alert.getId());
            dto.setReceiverEmail(alert.getReceiverEmail());
            dto.setText(alert.getText());
            dto.setType(alert.getWarningType());
            dto.setRegion(alert.getRegion());
            dto.setDangerPossibility(alert.getDangerPossibility());
            dto.setSeen(alert.isSeen());
            dto.setNotificationType("alert");
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
            notifications.add(dto);
        });

        return notifications;
    }

}
