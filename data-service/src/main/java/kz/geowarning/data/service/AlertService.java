package kz.geowarning.data.service;

import kz.geowarning.data.entity.FireRTData;
import kz.geowarning.data.entity.ForecastFireData;
import kz.geowarning.data.entity.dto.FireDataDTO;
import kz.geowarning.data.entity.dto.ForecastDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AlertService {

    @Value("${internal.notification.url}")
    private String notificationUrl;

    @Value("${internal.auth.url}")
    private String authUrl;

    private final RestTemplate restTemplate;
    private final FireRTDataService fireRTDataService;
    private final BingLocationsService bingLocationsService;
    private final MLDataService mlDataService;

    public AlertService(RestTemplate restTemplate, FireRTDataService fireRTDataService, BingLocationsService bingLocationsService, MLDataService mlDataService) {
        this.restTemplate = restTemplate;
        this.fireRTDataService = fireRTDataService;
        this.bingLocationsService = bingLocationsService;
        this.mlDataService = mlDataService;
    }

    public void alertRecipientsRealtime() throws JSONException, IOException {
        sendRealtimeNotificationsToRecipients(getRecipientsFromAuthService());
    }

    public void alertRecipientsForecast() throws JSONException, ParseException {
        sendForecastNotificationsToRecipients(getRecipientsFromAuthService());
    }

    private void sendForecastNotificationsToRecipients(List<Map<String, Object>> recipients) throws ParseException {
        for (Map<String, Object> user : recipients) {
            String firstName = (String) user.get("firstName");
            String lastName = (String) user.get("lastName");
            String email = (String) user.get("email");

            Map<String, Object> locationMap = (Map<String, Object>) user.get("location");
            String locationName = (String) locationMap.get("name");
            String latitude = String.valueOf(locationMap.get("latitude"));
            String longitude = String.valueOf(locationMap.get("longitude"));

            ForecastDTO forecastDTO = new ForecastDTO();
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime oneHourAgo = currentTime.minusHours(1);
            forecastDTO.setLatitude(latitude);
            forecastDTO.setLongitude(longitude);
            forecastDTO.setDateFrom(Timestamp.valueOf(oneHourAgo));
            forecastDTO.setDateTo(Timestamp.valueOf(currentTime));

            List<ForecastFireData> forecastList = mlDataService.getByFilter(forecastDTO);
            if (!forecastList.isEmpty()) {
                notifyWarningForecast(email, firstName, lastName, locationName, forecastList.get(0).getDangerLevel());
            }
        }
    }

    private void notifyWarningForecast(String email, String firstName, String lastName,
                                       String locationName, String dangerLevel) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("locationName", locationName);
        requestBody.put("level", dangerLevel);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(notificationUrl +
                        "/api/notification/service/notify-warning-forecast",
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("Forecast Notification. Response from server: " + responseBody);
        } else {
            System.out.println("Forecast Notification. Error: " + responseEntity.getStatusCode());
        }
    }

    private void notifyWarningRealtime(String email, String firstName, String lastName, String locationName,
                                      String count, List<String> fireOccurrences) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("locationName", locationName);
        requestBody.put("count", count);
        requestBody.put("fireOccurrences", fireOccurrences);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(notificationUrl +
                "/api/notification/service/notify-warning-realtime",
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("Real-time Notification. Response from server: " + responseBody);
        } else {
            System.out.println("Real-time Notification. Error: " + responseEntity.getStatusCode());
        }
    }

    private void sendRealtimeNotificationsToRecipients(List<Map<String, Object>> recipients) throws IOException {
        for (Map<String, Object> user : recipients) {
            String firstName = (String) user.get("firstName");
            String lastName = (String) user.get("lastName");
            String email = (String) user.get("email");

            Map<String, Object> locationMap = (Map<String, Object>) user.get("location");
            String locationName = (String) locationMap.get("name");
            String latitude = String.valueOf(locationMap.get("latitude"));
            String longitude = String.valueOf(locationMap.get("longitude"));

            FireDataDTO dataDTO = new FireDataDTO();
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalTime currentTime = LocalTime.now();
            dataDTO.setLatitude(latitude);
            dataDTO.setLongitude(longitude);
            dataDTO.setDateFrom(Date.valueOf(currentDate.format(formatter)));
            dataDTO.setTimeFrom(Time.valueOf(currentTime.minusHours(1)));
            dataDTO.setTimeTo(Time.valueOf(currentTime));

            List<FireRTData> firesList = fireRTDataService.getByFilter(dataDTO);
            List<String> fireOccurences = new ArrayList<>();
            for (FireRTData fire : firesList) {
                String formattedString = String.format("%s: %s.",
                        fire.getAcqTime(), bingLocationsService.getAddressInfoByCoordinates(fire.getLatitude(), fire.getLongitude()).getFormattedAddress());
                fireOccurences.add(formattedString);
            }
            if (!fireOccurences.isEmpty()) {
                notifyWarningRealtime(email, firstName, lastName, locationName, String.valueOf(fireOccurences.size()), fireOccurences);
            }
        }
    }

    private List<Map<String, Object>> getRecipientsFromAuthService() throws JSONException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(authUrl +
                "/internal/api/public/user/v1/users/emailRecipients", String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            JSONArray usersArray = new JSONArray(responseBody);

            List<Map<String, Object>> users = new ArrayList<>();
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", userObject.getLong("id"));
                userMap.put("firstName", userObject.getString("firstName"));
                userMap.put("lastName", userObject.getString("lastName"));
                userMap.put("email", userObject.getString("email"));

                JSONObject locationObject = userObject.getJSONObject("locationId");
                Map<String, Object> locationMap = new HashMap<>();
                locationMap.put("id", locationObject.getLong("id"));
                locationMap.put("name", locationObject.getString("name"));
                locationMap.put("latitude", locationObject.getDouble("latitude"));
                locationMap.put("longitude", locationObject.getDouble("longitude"));
                userMap.put("location", locationMap);

                users.add(userMap);
            }
            return users;
        } else {
            return Collections.emptyList();
        }
    }
}
