package kz.geowarning.notification.controller;

import kz.geowarning.common.api.NotificationClient;
import kz.geowarning.notification.service.NotificationService;
import kz.geowarning.notification.util.RestConstants;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping(RestConstants.BASE_REST + "/service")
public class NotificationController implements NotificationClient {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @SneakyThrows
    @PostMapping("/notify-warning")
    public ResponseEntity notifyWarning(@RequestParam String warningType, String userEmail, String region, String dangerPossibility) {
        try {
            service.notifyWarning(warningType, userEmail, region, dangerPossibility);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/verify-email")
    public ResponseEntity verifyEmail(@RequestParam String userEmail) {
        try {
            service.verifyEmail(userEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }
}
