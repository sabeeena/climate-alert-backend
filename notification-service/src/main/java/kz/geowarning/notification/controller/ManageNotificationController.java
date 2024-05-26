package kz.geowarning.notification.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import kz.geowarning.notification.dto.DeviceTokenDTO;
import kz.geowarning.notification.dto.NotificationDTO;
import kz.geowarning.notification.entity.AlertNotification;
import kz.geowarning.notification.entity.MobileDeviceToken;
import kz.geowarning.notification.entity.ReportNotification;
import kz.geowarning.notification.service.ManageNotificationService;
import kz.geowarning.notification.service.PushNotificationService;
import kz.geowarning.notification.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(RestConstants.BASE_REST + "/service")
public class ManageNotificationController {
    @Autowired
    private ManageNotificationService manageNotificationService;
    @Autowired
    private PushNotificationService pushNotificationService;


    @GetMapping("/alert")
    public ResponseEntity<List<AlertNotification>> getAllAlertNotifications(@RequestParam String email) {
        List<AlertNotification> alertNotifications = manageNotificationService.getAllAlertNotifications(email);
        return ResponseEntity.ok(alertNotifications);
    }

    @PostMapping("/save-device-token")
    public ResponseEntity<MobileDeviceToken> saveToken(@RequestBody DeviceTokenDTO deviceTokenDTO) {
        return ResponseEntity.ok(pushNotificationService.saveToken(deviceTokenDTO));
    }
    @PostMapping("/send-mobile-notification")
    public ResponseEntity<?> sendMobileNotification() throws FirebaseMessagingException, IOException {
        return ResponseEntity.ok(pushNotificationService.sendMobileNotification());
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

    @GetMapping("/get-by-id")
    public NotificationDTO getReportNotificationById(@RequestParam Long id, String notificationType) throws RuntimeException, RuntimeException {
        return ResponseEntity.ok(manageNotificationService.getReportNotificationById(id, notificationType)).getBody();
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
    public ResponseEntity<String> markAlertAsSeen(@PathVariable Long id) throws RuntimeException {
        manageNotificationService.markAlertAsSeen(id);
        return ResponseEntity.ok("Alert Notification marked as seen");
    }

    // Mark report as seen by ID
    @PutMapping("/report/seen/{id}")
    public ResponseEntity<String> markReportAsSeen(@PathVariable Long id) throws RuntimeException {
        manageNotificationService.markReportAsSeen(id);
        return ResponseEntity.ok("Report Notification marked as seen");
    }

}
