package kz.geowarning.notification.notificationservice.controller;

import kz.geowarning.notification.notificationservice.service.NotificationService;
import kz.geowarning.notification.notificationservice.util.RestConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping(RestConstants.BASE_REST)
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping("/notify-warning")
    public ResponseEntity<?> notifyWarning(@RequestParam String warningType, String userEmail, String region, String dangerPossibility) throws MessagingException {
        service.notifyWarning(warningType, userEmail, region, dangerPossibility);
        return ResponseEntity.ok(200);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String userEmail) throws MessagingException {
        service.verifyEmail(userEmail);
        return ResponseEntity.ok(200);
    }
}
