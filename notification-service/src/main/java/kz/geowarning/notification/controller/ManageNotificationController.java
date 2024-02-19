package kz.geowarning.notification.controller;

import kz.geowarning.common.exceptions.NotFoundException;
import kz.geowarning.notification.dto.NotificationDTO;
import kz.geowarning.notification.entity.AlertNotification;
import kz.geowarning.notification.entity.ReportNotification;
import kz.geowarning.notification.repository.AlertNotificationRepository;
import kz.geowarning.notification.repository.ReportNotificationRepository;
import kz.geowarning.notification.service.ManageNotificationService;
import kz.geowarning.notification.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.BASE_REST + "/service")
public class ManageNotificationController {
    @Autowired
    private ManageNotificationService manageNotificationService;


    @GetMapping("/alert")
    public ResponseEntity<List<AlertNotification>> getAllAlertNotifications() {
        List<AlertNotification> alertNotifications = manageNotificationService.getAllAlertNotifications();
        return ResponseEntity.ok(alertNotifications);
    }

    @GetMapping("/report")
    public ResponseEntity<List<ReportNotification>> getAllReportNotifications() {
        List<ReportNotification> reportNotifications = manageNotificationService.getAllReportNotifications();
        return ResponseEntity.ok(reportNotifications);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(@RequestParam String email) {
        List<NotificationDTO> notifications = manageNotificationService.getAllNotifications(email);
        return ResponseEntity.ok(notifications);
    }


    // Delete alert by ID
    @DeleteMapping("/alert/{id}")
    public ResponseEntity<String> deleteAlertNotification(@PathVariable Long id) {
        manageNotificationService.deleteAlertNotification(id);
        return ResponseEntity.ok("Alert Notification deleted successfully");
    }

    // Delete report by ID
    @DeleteMapping("/report/{id}")
    public ResponseEntity<String> deleteReportNotification(@PathVariable Long id) {
        manageNotificationService.deleteReportNotification(id);
        return ResponseEntity.ok("Report Notification deleted successfully");
    }

    // Mark alert as seen by ID
    @PutMapping("/alert/seen/{id}")
    public ResponseEntity<String> markAlertAsSeen(@PathVariable Long id) throws NotFoundException {
        manageNotificationService.markAlertAsSeen(id);
        return ResponseEntity.ok("Alert Notification marked as seen");
    }

    // Mark report as seen by ID
    @PutMapping("/report/seen/{id}")
    public ResponseEntity<String> markReportAsSeen(@PathVariable Long id) throws NotFoundException {
        manageNotificationService.markReportAsSeen(id);
        return ResponseEntity.ok("Report Notification marked as seen");
    }

}
