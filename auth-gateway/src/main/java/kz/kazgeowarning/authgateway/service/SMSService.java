package kz.kazgeowarning.authgateway.service;

import kz.kazgeowarning.authgateway.model.User;
import kz.kazgeowarning.authgateway.security.service.impl.UsersService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SMSService {

    @Value("${internal.url.notification}")
    private String notificationUrl;

    private final RestTemplate restTemplate;
    private final UsersService usersService;

    public SMSService(RestTemplate restTemplate, UsersService usersService) {
        this.restTemplate = restTemplate;
        this.usersService = usersService;
    }

    public ResponseEntity<String> verifyEmail(String phoneNumber, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("phoneNumber", phoneNumber);
        requestBody.put("code", code);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(notificationUrl +
                        "/api/notification/service/checkVerificationCode", requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK && Objects.equals(response.getBody(), "Valid")) {
            User user = usersService.findUserByPhone(phoneNumber);
            user.setNotifySms(true);
            usersService.save(user);
            return ResponseEntity.ok("Phone number verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Verification code is not valid, try again.");
        }
    }
}
