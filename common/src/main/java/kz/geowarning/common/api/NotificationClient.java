package kz.geowarning.common.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NotificationClient", url = "${app.gateway.url}", path = "/api/notification/service")
public interface NotificationClient {
    @PostMapping("/verify-email")
    ResponseEntity verifyEmail(
            @RequestParam("userEmail") String userEmail
    );

    @PostMapping("/notify-warning")
    ResponseEntity notifyWarning(
            @RequestParam("warningType") String warningType,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("region") String region,
            @RequestParam("dangerPossibility") String dangerPossibility
    );
}
