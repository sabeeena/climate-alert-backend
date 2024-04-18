package kz.geowarning.notification.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.Notification;
import kz.geowarning.notification.dto.DeviceTokenDTO;
import kz.geowarning.notification.entity.MobileDeviceToken;
import kz.geowarning.notification.repository.MobileDeviceTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class PushNotificationService {

    @Autowired
    MobileDeviceTokenRepository mobileDeviceTokenRepository;

    public MobileDeviceToken saveToken(DeviceTokenDTO deviceTokenDTO) {
        String deviceToken = deviceTokenDTO.getDeviceToken();
        MobileDeviceToken existingToken = mobileDeviceTokenRepository.findByDeviceToken(deviceToken);

        if(existingToken != null) {
            return existingToken;
        } else {
            MobileDeviceToken mobileDeviceToken = new MobileDeviceToken();
            mobileDeviceToken.setDeviceToken(deviceToken);
            mobileDeviceToken.setUserEmail(deviceTokenDTO.getUserEmail());
            return mobileDeviceTokenRepository.save(mobileDeviceToken);
        }
    }


    public Object sendMobileNotification() throws FirebaseMessagingException, IOException {
        List<MobileDeviceToken> mobileDeviceTokens = mobileDeviceTokenRepository.findAll();
        for(MobileDeviceToken mobileDeviceToken: mobileDeviceTokens) {
            sendPushNotification(
                    mobileDeviceToken.getDeviceToken(),
                    "Kazgeowarning mobile notification",
                    "BODY"
                    );
        }
        return true;
    }

    public void sendPushNotification(String deviceToken, String title, String body) throws FirebaseMessagingException, IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("kazgeowarning-firebase-adminsdk-ctvol-ca8a82cec3.json");
            if (serviceAccount == null) {
                System.out.println("Файл не найден");
            } else {
                System.out.println("Файл найден");
            }
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }

        Message message = Message.builder()
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .putData("title", title)
                .putData("body", body)
                .setToken(deviceToken)
                .build();
        System.out.println(deviceToken);
        System.out.println(message);
        FirebaseMessaging.getInstance().send(message);
    }


}
