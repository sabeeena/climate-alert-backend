package kz.geowarning.data.service;

import kz.geowarning.data.entity.EarthquakeData;
import kz.geowarning.data.entity.FireRTData;
import kz.geowarning.data.entity.ForecastFireData;
import kz.geowarning.data.entity.dto.FireDataDTO;
import kz.geowarning.data.entity.dto.ForecastDTO;
import kz.geowarning.data.util.LocationUtil;
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
import java.text.SimpleDateFormat;
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
        sendRealtimeNotificationsToRecipients(getSMSReceiversFromAuthService());
    }

    public void alertRecipientsForecast() throws JSONException, ParseException {
        sendForecastNotificationsToRecipients(getRecipientsFromAuthService());
        sendForecastNotificationsToRecipients(getSMSReceiversFromAuthService());
    }

    public void alertRecipientsEarthquake(EarthquakeData earthquakeData) throws JSONException {
        sendEarthquakeNotifications(getRecipientsFromAuthService(), earthquakeData);
        sendEarthquakeNotifications(getSMSReceiversFromAuthService(), earthquakeData);
    }

    private void sendEarthquakeNotifications(List<Map<String, Object>> recipients, EarthquakeData earthquakeData) {
        for (Map<String, Object> user : recipients) {
            String firstName = (String) user.get("firstName");
            String lastName = (String) user.get("lastName");
            String email = (String) user.get("email");
            String phoneNumber = (String) user.get("phoneNumber");

            Map<String, Object> locationMap = (Map<String, Object>) user.get("location");
            String latitude = String.valueOf(locationMap.get("latitude"));
            String longitude = String.valueOf(locationMap.get("longitude"));

            // The range is approximate, taken that 9 magnitude earthquake can cause damage to
            // structures in areas over 1000 km across where people live.
            if (LocationUtil.isWithinRange(Double.parseDouble(latitude), Double.parseDouble(longitude),
                    Double.parseDouble(earthquakeData.getLatitude()), Double.parseDouble(earthquakeData.getLongitude()),
                    1000)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    notifyEarthquakeEmail(email, firstName, lastName, earthquakeData.getPlace(), earthquakeData.getMag(),
                            dateFormat.format(earthquakeData.getTime()));
                } else {
                    notifyEarthquakeSMS(phoneNumber, firstName, lastName, earthquakeData.getPlace(), earthquakeData.getMag(),
                            dateFormat.format(earthquakeData.getTime()));
                }
            }
        }
    }

    private void sendForecastNotificationsToRecipients(List<Map<String, Object>> recipients) throws ParseException {
        for (Map<String, Object> user : recipients) {
            String firstName = (String) user.get("firstName");
            String lastName = (String) user.get("lastName");
            String email = (String) user.get("email");
            String phoneNumber = (String) user.get("phoneNumber");

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
                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    notifyWarningForecast(email, firstName, lastName, locationName, forecastList.get(0).getDangerLevel());
                } else {
                    notifySMSForecastFires(phoneNumber, firstName, lastName, locationName, forecastList.get(0).getDangerLevel());
                }
            }
        }
    }

    private void notifyEarthquakeEmail(String email, String firstName, String lastName, String locationName,
                                       String magnitude, String time) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("phoneNumber", null);
        requestBody.put("email", email);
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("locationName", locationName);
        requestBody.put("magnitude", magnitude);
        requestBody.put("time", time);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(notificationUrl +
                        "/api/notification/service/notify-email-earthquake",
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("Earthquake Notification. Response from server: " + responseBody);
        } else {
            System.out.println("Earthquake Notification. Error: " + responseEntity.getStatusCode());
        }
    }

    private void notifyEarthquakeSMS(String phoneNumber, String firstName, String lastName, String locationName,
                                     String magnitude, String time) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("phoneNumber", phoneNumber);
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("locationName", locationName);
        requestBody.put("magnitude", magnitude);
        requestBody.put("time", time);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(notificationUrl +
                        "/api/notification/service/notify-sms-earthquake",
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("Earthquake SMS Notification. Response from server: " + responseBody);
        } else {
            System.out.println("Earthquake SMS Notification. Error: " + responseEntity.getStatusCode());
        }
    }

    private void notifyWarningForecast(String email, String firstName, String lastName,
                                       String locationName, String dangerLevel) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("phoneNumber", null);
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

    private void notifySMSForecastFires(String phoneNumber, String firstName, String lastName,
                                        String locationName, String dangerLevel) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("phoneNumber", phoneNumber);
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("locationName", locationName);
        requestBody.put("level", dangerLevel);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(notificationUrl +
                        "/api/notification/service/notify-forecast-fire-sms",
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("Forecast Fire SMS Notification. Response from server: " + responseBody);
        } else {
            System.out.println("Forecast FIre SMS Notification. Error: " + responseEntity.getStatusCode());
        }
    }

    private void notifySMSRealtimeFires(String phoneNumber, String firstName, String lastName, String locationName,
                                        String count, List<String> fireOccurrences) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("phoneNumber", phoneNumber);
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("locationName", locationName);
        requestBody.put("count", count);
        requestBody.put("fireOccurrences", fireOccurrences);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(notificationUrl +
                        "/api/notification/service/notify-fire-sms",
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("Real-time Fire SMS Notification. Response from server: " + responseBody);
        } else {
            System.out.println("Real-time FIre SMS Notification. Error: " + responseEntity.getStatusCode());
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
        requestBody.put("phoneNumber", null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(notificationUrl +
                "/api/notification/service/notify-warning-realtime",
                requestEntity,
                String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("Real-time Fire Email Notification. Response from server: " + responseBody);
        } else {
            System.out.println("Real-time Fire Email Notification. Error: " + responseEntity.getStatusCode());
        }
    }

    private void sendRealtimeNotificationsToRecipients(List<Map<String, Object>> recipients) throws IOException {
        for (Map<String, Object> user : recipients) {
            String firstName = (String) user.get("firstName");
            String lastName = (String) user.get("lastName");
            String email = (String) user.get("email");
            String phoneNumber = (String) user.get("phoneNumber");

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
            List<String> fireOccurrences = new ArrayList<>();
            for (FireRTData fire : firesList) {
                String formattedString = String.format("%s: %s.",
                        fire.getAcqTime(), bingLocationsService.getAddressInfoByCoordinates(fire.getLatitude(), fire.getLongitude()).getFormattedAddress());
                fireOccurrences.add(formattedString);
            }
            if (!fireOccurrences.isEmpty()) {
                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    notifyWarningRealtime(email, firstName, lastName, locationName, String.valueOf(fireOccurrences.size()), fireOccurrences);
                } else {
                    notifySMSRealtimeFires(phoneNumber, firstName, lastName, locationName, String.valueOf(fireOccurrences.size()), fireOccurrences);
                }
            }
        }
    }

    private List<Map<String, Object>> getSMSReceiversFromAuthService() throws JSONException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(authUrl +
                "/internal/api/public/user/v1/users/smsReceivers", String.class);
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
                userMap.put("phoneNumber", userObject.getString("phoneNumber"));

                JSONObject locationObject = userObject.getJSONObject("locationId");
                Map<String, Object> locationMap = new HashMap<>();
                locationMap.put("id", locationObject.getLong("id"));
                locationMap.put("name", locationObject.getString("name"));
                locationMap.put("latitude", locationObject.getDouble("latitude"));
                locationMap.put("longitude", locationObject.getDouble("longitude"));
                userMap.put("location", locationMap);

                users.add(userMap);
            } return users;
        } else {
            return Collections.emptyList();
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
