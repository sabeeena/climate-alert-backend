package kz.geowarning.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NotificationService", url = "${app.notification.url}/api/notification")
public interface NotificationServiceClient {

    @PostMapping("/verify-email")
    void verifyEmail(
            @RequestParam("userEmail") String userEmail
    );
    @PostMapping("/notify-warning")
    void notifyUser(
            @RequestParam("warningType") String warningType,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("region") String region,
            @RequestParam("dangerPossibility") String dangerPossibility
    );
}