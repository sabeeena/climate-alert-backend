package kz.geowarning.notification.controller;

import kz.geowarning.notification.dto.ForecastNotificationContentDTO;
import kz.geowarning.notification.dto.RealTimeNotificationContentDTO;
import kz.geowarning.notification.dto.ReportNotificationDTO;
import kz.geowarning.notification.service.NotificationService;
import kz.geowarning.notification.util.RestConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(RestConstants.BASE_REST + "/service")
public class NotificationController {

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
    @PostMapping("/notify-warning-realtime")
    public ResponseEntity notifyWarningRealtime(@RequestBody RealTimeNotificationContentDTO contentDTO) {
        try {
            service.notifyRealtime(contentDTO);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/notify-warning-forecast")
    public ResponseEntity notifyWarningForecast(@RequestBody ForecastNotificationContentDTO contentDTO) {
        try {
            service.notifyForecast(contentDTO);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/notify-report")
    public ResponseEntity reportNotify(@RequestBody ReportNotificationDTO reportNotificationDTO) {
        try {
            service.reportNotify(reportNotificationDTO);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String userEmail) {
        try {
            service.verifyEmail(userEmail);
            return ResponseEntity.ok("Email verification initiated successfully.");
        } catch (MessagingException e) {
            log.error("Error occurred while verifying email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email verification failed.");
        }
    }

    @SneakyThrows
    @PostMapping("/sendSms")
    public ResponseEntity sendSMS(@RequestParam String recipient, String text) {
        return service.sendSMSNotification(recipient, text);
    }

    @PostMapping("/sendSMSCode")
    public ResponseEntity<String> sendSMSVerificationCode(@RequestParam String phoneNumber) throws Exception {
        return service.sendVerificationCode(phoneNumber);
    }

    @SneakyThrows
    @PostMapping("/checkVerificationCode")
    public ResponseEntity<String> checkVerificationCode(@RequestBody Map<String, String> params) {
        return ResponseEntity.ok(service.checkVerificationCode(params.get("phoneNumber"), params.get("code")));
    }
}
//http://localhost:8083/api/notification/service/notify-warning?warningType=earthquake&userEmail=adilbayevameruert@gmail.com&region=almaty&dangerPossibility=123
