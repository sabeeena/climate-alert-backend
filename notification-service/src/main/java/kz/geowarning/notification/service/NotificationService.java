package kz.geowarning.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import kz.geowarning.notification.dto.EarthquakeNotificationDTO;
import kz.geowarning.notification.dto.ForecastNotificationContentDTO;
import kz.geowarning.notification.dto.RealTimeNotificationContentDTO;
import kz.geowarning.notification.dto.ReportNotificationDTO;
import kz.geowarning.notification.entity.AlertNotification;
import kz.geowarning.notification.entity.MobileDeviceToken;
import kz.geowarning.notification.entity.ReportNotification;
import kz.geowarning.notification.entity.VerificationCode;
import kz.geowarning.notification.repository.AlertNotificationRepository;
import kz.geowarning.notification.repository.MobileDeviceTokenRepository;
import kz.geowarning.notification.repository.ReportNotificationRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class NotificationService {
    @Autowired
    private SMTPService iEmailService;

    @Value("${application.link.verification-link}")
    private String verificationLink;
    @Autowired
    private AlertNotificationRepository alertNotificationRepository;
    @Autowired
    private ReportNotificationRepository reportNotificationRepository;
    @Autowired
    private PushNotificationService pushNotificationService;
    @Autowired
    private MobileDeviceTokenRepository mobileDeviceTokenRepository;
    @Autowired
    private SMSService smsService;
    @Autowired
    private VerificationCodeService verificationCodeService;

    public ResponseEntity sendSMSNotification(String recipient, String text) {
        try {
            return ResponseEntity.ok()
                    .body(smsService.sendSMSMessage(recipient, text));
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body("SMS service is temporarily unavailable");
        }
    }

    public String checkVerificationCode(String phoneNumber, String code) {
        if (verificationCodeService.checkVerificationCode(phoneNumber, code).isPresent()) {
            return "Valid";
        } else {
            return "Not Valid";
        }
    }

    public ResponseEntity<String> sendVerificationCode(String phoneNumber) throws Exception {
        verificationCodeService.disableAllOverdueCodesByPhone(phoneNumber);
        String code;
        if (verificationCodeService.getActiveCode(phoneNumber).isEmpty()) {
            code = generateCode();
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setPhoneNumber(phoneNumber);
            verificationCode.setCode(code);
            verificationCode.setCreateDate(LocalDateTime.now());
            verificationCode.setSendDate(LocalDateTime.now());
            verificationCode.setActive(true);
            verificationCodeService.saveVerificationCode(verificationCode);
        } else {
            VerificationCode activeCode = verificationCodeService.getActiveCode(phoneNumber).get(0);
            if (Duration.between(activeCode.getSendDate(), LocalDateTime.now()).toMinutes() < 2) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body("Too many requests. Please wait 2 minutes before trying again.");
            }
            activeCode.setSendDate(LocalDateTime.now());
            verificationCodeService.saveVerificationCode(activeCode);
            code = activeCode.getCode();
        }
        sendSMSNotification(phoneNumber, code + ". This is your verification code on KazGeoWarning. Don't share it.");
        return ResponseEntity.ok("Verification code sent.");
    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void notifyWarning(String warningType, String userEmail, String region, String dangerPossibility) throws MessagingException, IOException, FirebaseMessagingException {
        String body = generateWarningMessage(region, userEmail, warningType, dangerPossibility);
        iEmailService.sendMail(userEmail, generateWarningSubject(region, dangerPossibility), body);
        sendNotificationMobile(userEmail, body);
    }

    public void sendNotificationMobile(String userEmail, String body) throws IOException, FirebaseMessagingException {
        List<MobileDeviceToken> mobileDeviceTokens = mobileDeviceTokenRepository.findAll();
        for(MobileDeviceToken mobileDeviceToken: mobileDeviceTokens) {
            if(Objects.equals(mobileDeviceToken.getUserEmail(), userEmail)) {
                pushNotificationService.sendPushNotification(
                        mobileDeviceToken.getDeviceToken(),
                        "Kazgeowarning",
                        body
                );
            }

        }
//        pushNotificationService.sendMobileNotification();
    }

    public void notifyRealtime(RealTimeNotificationContentDTO contentDTO) throws MessagingException, IOException, FirebaseMessagingException {
        String body = generateWarningMessageRealTime(contentDTO);
        iEmailService.sendMail(contentDTO.getEmail(), generateWarningSubjectRealtime(contentDTO), body);
        sendNotificationMobile(contentDTO.getEmail(), body);
    }

    public void notifySMSFireRealtime(RealTimeNotificationContentDTO contentDTO) {
        String body = generateWarningMessageRealTime(contentDTO);
        sendSMSNotification(contentDTO.getPhoneNumber(), Jsoup.parse(body).text());
    }

    public void notifySMSFireForecast(ForecastNotificationContentDTO contentDTO) {
        String body = generateWarningMessageForecast(contentDTO);
        sendSMSNotification(contentDTO.getPhoneNumber(), Jsoup.parse(body).text());
    }

    public void notifyForecast(ForecastNotificationContentDTO contentDTO) throws MessagingException, IOException, FirebaseMessagingException {
        String body = generateWarningMessageForecast(contentDTO);
        iEmailService.sendMail(contentDTO.getEmail(), generateWarningSubjectForecast(contentDTO), generateWarningMessageForecast(contentDTO));
        sendNotificationMobile(contentDTO.getEmail(), body);
    }

    public void notifyEarthquake(EarthquakeNotificationDTO contentDTO) throws IOException, FirebaseMessagingException, MessagingException {
        String body = generateEarthquakeMessage(contentDTO);
        iEmailService.sendMail(contentDTO.getEmail(), generateEarthquakeSubject(contentDTO), generateEarthquakeMessage(contentDTO));
        sendNotificationMobile(contentDTO.getEmail(), body);
    }

    public void notifySMSEarthquake(EarthquakeNotificationDTO contentDTO) {
        String body = generateEarthquakeMessage(contentDTO);
        sendSMSNotification(contentDTO.getPhoneNumber(), Jsoup.parse(body).text());
    }

    private String generateEarthquakeSubject(EarthquakeNotificationDTO contentDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String currentDateTimeString = dateFormat.format(currentDate);
        String subject = "Зафиксировано землетрясение в " + currentDateTimeString + " в " + contentDTO.getLocationName() + ".\n";
        return subject;
    }

    private String generateEarthquakeMessage(EarthquakeNotificationDTO contentDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String currentDateTimeString = dateFormat.format(currentDate);

        String message = "<span style=\"font-family: Arial; font-size: 16px;\"><u>" + currentDateTimeString + "</u><br><br><br>";
        message += "Уважаемый(ая) <b>" + contentDTO.getFirstName() + " " + contentDTO.getLastName() + "</b>, <br>";
        message += "В пределах вашей локации было зафиксировано землетрясение в <b>" + contentDTO.getTime()
                + "</b> магнитудой в <b>" + contentDTO.getMagnitude() + "</b> баллов по шкале Рихтера"
                + ", установленный эпицентр - <b>" + contentDTO.getLocationName() + "</b>.<br><br><br>";
        message += "Если у вас есть какие-либо вопросы или требуется дополнительная информация, пожалуйста, ";
        message += "свяжитесь с нашей службой поддержки.<br><br>";
        message += "С уважением,<br>";
        message += "Команда <b>KazGeoWarning!</b><br><br>";
        message += "</span>";

        String saveMessage = currentDateTimeString + ". ";
        saveMessage += "Уважаемый(ая) " + contentDTO.getFirstName() + " " + contentDTO.getLastName() + ", ";
        saveMessage += "В пределах вашей локации было зафиксировано землетрясение в " + contentDTO.getTime()
                + " магнитудой в " + contentDTO.getMagnitude() + " баллов по шкале Рихтера"
                + ", установленный эпицентр - " + contentDTO.getLocationName() + ". ";
        saveMessage += "Если у вас есть какие-либо вопросы или требуется дополнительная информация, пожалуйста, ";
        saveMessage += "свяжитесь с нашей службой поддержки. ";
        saveMessage += "С уважением, ";
        saveMessage += "Команда KazGeoWarning! ";

        if (contentDTO.getPhoneNumber() == null || contentDTO.getPhoneNumber().isEmpty()) {
            AlertNotification alertNotification = new AlertNotification();
            alertNotification.setReceiverEmail(contentDTO.getEmail());
            alertNotification.setSenderEmail("KazGeoWarning");
            alertNotification.setWarningType("real-time fire");
            alertNotification.setText(Jsoup.parse(saveMessage).text());
            alertNotification.setSeen(false);
            alertNotification.setSentTime(LocalDateTime.now());
            alertNotificationRepository.save(alertNotification);
        }

        return message;
    }

    public String generateWarningMessage(String region, String userEmail, String warningType, String dangerPossibility){
        String message = "Уважаемый пользователь," + userEmail + "\n\n";
        message += "Мы обнаружили " + warningType + " с возможностью опасности " + dangerPossibility + "% ";
        message += "Примите немедленные меры для обеспечения безопасности.\n";
        message += "Если у вас есть какие-либо вопросы или требуется дополнительная информация, пожалуйста, ";
        message += "свяжитесь с нашей службой поддержки.\n\n\n";
        message += "С уважением,\n";
        message += "Команда kazgeowarning!\n\n";

//        message += "Құрметті пайдаланушы," + userEmail + "\n\n";
//        message += "Біз ескерту түрі " + warningType + " қауіп ықтималдығы " + dangerPossibility + "% жарамдықтың мүмкіндігін анықтадық. ";
//        message += "Егер сізде сұрауларыңыз немесе көмек қажет болса, алдыңғы көмек қызметімен байланысуыңызды сұраймыз.\n\n";
//        message += "Сіздің " + "kazgeowarning" + " қомандасы!.\n\n\n";
//
//        message += "Dear user," + userEmail + "\n\n";
//        message += "We have detected a " + warningType + " with a possibility of danger of " + dangerPossibility + "% ";
//        message += "Please take immediate action to ensure safety. ";
//        message += "If you have any questions or need further information, please contact our support team.\n\n";
//        message += "Sincerely,\n";
//        message += "The kazgeowarning team!";

        AlertNotification alertNotification = new AlertNotification();
        alertNotification.setReceiverEmail(userEmail);
        alertNotification.setWarningType(warningType);
        alertNotification.setText(message);
        alertNotification.setSeen(false);
        alertNotification.setRegion(region);
        alertNotification.setDangerPossibility(dangerPossibility);
        alertNotification.setSentTime(LocalDateTime.now());
        alertNotificationRepository.save(alertNotification);
        return message;
    }

    public String generateWarningMessageRealTime(RealTimeNotificationContentDTO contentDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String currentDateTimeString = dateFormat.format(currentDate);

        String message = "<span style=\"font-family: Arial; font-size: 16px;\"><u>" + currentDateTimeString + "</u><br><br><br>";
        message += "Уважаемый(ая) <b>" + contentDTO.getFirstName() + " " + contentDTO.getLastName() + "</b>, <br>";
        message += "За последний час возле <b>" + contentDTO.getLocationName() + "</b> было обнаружено <b>" + contentDTO.getCount() + "</b> пожаров.<br><br>";
        message += "<u>Приблизительные местоположения:</u><br>";
        for (String row : contentDTO.getFireOccurrences()) {
            message += row + "<br>";
        }
        message += "<br><br>Если у вас есть какие-либо вопросы или требуется дополнительная информация, пожалуйста, ";
        message += "свяжитесь с нашей службой поддержки.<br><br>";
        message += "С уважением,<br>";
        message += "Команда <b>KazGeoWarning!</b><br><br>";
        message += "</span>";

        String saveMessage = currentDateTimeString + ". ";
        saveMessage += "Уважаемый(ая) " + contentDTO.getFirstName() + " " + contentDTO.getLastName() + ", ";
        saveMessage += "За последний час возле " + contentDTO.getLocationName() + " было обнаружено " + contentDTO.getCount() + " пожаров. ";
        saveMessage += "Приблизительные местоположения: ";
        for (String row : contentDTO.getFireOccurrences()) {
            saveMessage += row + " ";
        }
        saveMessage += "Если у вас есть какие-либо вопросы или требуется дополнительная информация, пожалуйста, ";
        saveMessage += "свяжитесь с нашей службой поддержки. ";
        saveMessage += "С уважением, ";
        saveMessage += "Команда KazGeoWarning! ";

        if (contentDTO.getPhoneNumber() == null || contentDTO.getPhoneNumber().isEmpty()) {
            AlertNotification alertNotification = new AlertNotification();
            alertNotification.setReceiverEmail(contentDTO.getEmail());
            alertNotification.setSenderEmail("KazGeoWarning");
            alertNotification.setWarningType("real-time fire");
            alertNotification.setText(Jsoup.parse(saveMessage).text());
            alertNotification.setSeen(false);
            alertNotification.setSentTime(LocalDateTime.now());
            alertNotificationRepository.save(alertNotification);
        }

        return message;
    }

    public String generateWarningSubjectRealtime(RealTimeNotificationContentDTO contentDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String currentDateTimeString = dateFormat.format(currentDate);
        String subject = "Информация о пожарах на " + currentDateTimeString + " в " + contentDTO.getLocationName() + ".\n";
        return subject;
    }

    public String generateWarningMessageForecast(ForecastNotificationContentDTO contentDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String currentDateTimeString = dateFormat.format(currentDate);

        String message = "<span style=\"font-family: Arial; font-size: 16px;\"><u>" + currentDateTimeString + "</u><br><br><br>";
        message += "Уважаемый(ая) <b>" + contentDTO.getFirstName() + " " + contentDTO.getLastName() + "</b>, <br>";
        message += "В пределах региона <b>" + contentDTO.getLocationName() + "</b> был обнаружен уровень пожарной опасности: <b>" + contentDTO.getLevel() + "</b>.<br><br><br>";
        message += "Если у вас есть какие-либо вопросы или требуется дополнительная информация, пожалуйста, ";
        message += "свяжитесь с нашей службой поддержки.<br><br>";
        message += "С уважением,<br>";
        message += "Команда <b>KazGeoWarning!</b><br><br>";
        message += "</span>";


        String saveMessage = currentDateTimeString + ".  ";
        saveMessage += "Уважаемый(ая) " + contentDTO.getFirstName() + " " + contentDTO.getLastName() + ", ";
        saveMessage += "В пределах региона " + contentDTO.getLocationName() + " был обнаружен уровень опасности: " + contentDTO.getLevel() + ".  ";
        saveMessage += "Если у вас есть какие-либо вопросы или требуется дополнительная информация, пожалуйста, ";
        saveMessage += "свяжитесь с нашей службой поддержки.  ";
        saveMessage += "С уважением, ";
        saveMessage += "Команда KazGeoWarning!  ";

        if (contentDTO.getPhoneNumber() == null || contentDTO.getPhoneNumber().isEmpty()) {
            AlertNotification alertNotification = new AlertNotification();
            alertNotification.setReceiverEmail(contentDTO.getEmail());
            alertNotification.setSenderEmail("KazGeoWarning");
            alertNotification.setWarningType("forecast fire");
            alertNotification.setText(Jsoup.parse(saveMessage).text());
            alertNotification.setSeen(false);
            alertNotification.setSentTime(LocalDateTime.now());
            alertNotificationRepository.save(alertNotification);
        }

        return message;
    }

    public String generateWarningSubjectForecast(ForecastNotificationContentDTO contentDTO) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        String currentDateTimeString = dateFormat.format(currentDate);
        String subject = "Уровень пожароопасности в " + contentDTO.getLocationName() + " на " + currentDateTimeString + ".\n";
        return subject;
    }

    public String generateWarningSubject(String region, String dangerPossibility) {
        String subject ="Внимание! Уровень опасности в вашем регионе " + region +" составляет " + dangerPossibility + "%\n";
        subject += "Назар аударыңыз! Сіздің аймағыңыздағы қауіп деңгейі "+ region + "" + dangerPossibility + "% сәйкес келеді\n";
        subject += "Attention! The risk level in your area corresponds to "+ region + "" + dangerPossibility + "%";
        return subject;
    }

    public void verifyEmail(String userEmail) throws MessagingException {
        iEmailService.sendMail(userEmail, generateVerifySubject(userEmail), generateVerifyMessage(userEmail));
    }

    private String generateVerifyMessage(String userEmail) {
        String message = "";

        message += "Привет "+ userEmail + ",\n\n";
        message += "Вы зарегистрировали учетную запись на kazgeowarning портале, прежде чем сможете использовать свою учетную запись, вам необходимо подтвердить, что это ваш адрес электронной почты, нажав здесь: " + verificationLink+userEmail + "\n\n";
        message += "С наилучшими пожеланиями, kazgeowarning\n\n\n";

//        message += "Сәлем "+ userEmail + ",\n\n";
//        message += "Есептік жазбаңызды пайдаланбас бұрын kazgeowarning порталда тіркелгіге тіркелдіңіз, бұл сіздің электрондық пошта екенін мына жерді басу арқылы растауыңыз керек: " + verificationLink +userEmail + "\n\n";
//        message += "Ізгі ниетпен, kazgeowarning\n\n\n";
//
//        message += "Hello " + userEmail + ",\n\n";
//        message += "You registered an account on kazgeowarning. Before being able to use your account, you need to verify that this is your email address by clicking here: " + verificationLink+userEmail + "\n\n";
//        message += "Kind Regards, kazgeowarning";
        return message;
    }

    private String generateVerifySubject(String userEmail) {
        String subject = "";

        subject += "Добро пожаловать на kazgeowarning, пожалуйста, подтвердите свой адрес электронной почты\n";
//        subject += "Kazgeowarning порталға қош келдіңіз, электрондық пошта мекенжайыңызды растаңыз\n";
//        subject += "Welcome to kazgeowarning, please verify your email address";

        return subject;
    }

    private String generateReportSubject() {
        String subject = "";

        subject += "Запрос на согласование отчета от редактора.\n";

        return subject;
    }

    private String generateReportMessage(ReportNotificationDTO reportNotificationDTO) {
        String message = "";

        message += "Вам был отправлен запрос на согласования отчета. Пожалуйста, рассмотрите в ближайшем времени.\n";

        ReportNotification reportNotification = new ReportNotification();
        reportNotification.setReceiverEmail(reportNotificationDTO.getReceiverEmail());
        reportNotification.setSenderEmail(reportNotificationDTO.getSenderEmail());
        reportNotification.setText(message);
        reportNotification.setTypeStatus("согласование");
        reportNotification.setReportType(reportNotificationDTO.getReportType());
        reportNotification.setReportId(reportNotificationDTO.getReportId());
        reportNotification.setSeen(false);
        reportNotification.setSentTime(LocalDateTime.now());
        reportNotificationRepository.save(reportNotification);
        return message;
    }

    private String generateReportSubjectAdmin(String type) {
        String message = "empty something is wrong";

        if(Objects.equals(type, "Approve")){
            return "Успешное согласование репорта.\n";
        } else if(Objects.equals(type, "Correction")) {
            return "Возврат на корректировку репорта.\n";
        }

        return message;
    }

    public String generateReportMessageAdmin(String type, ReportNotificationDTO reportNotificationDTO){
        String message = "empty something is wrong";

        if(Objects.equals(type, "Approve")){
            message = "Хочу сообщить, что отчет был успешно согласован от имени администратора.\n";
        } else if(Objects.equals(type, "Correction")) {
            message = "Хочу сообщить, что отчет был отправлен на корректировку.\n";
        }
        ReportNotification reportNotification = new ReportNotification();
        reportNotification.setSenderEmail(reportNotificationDTO.getReceiverEmail());
        reportNotification.setReceiverEmail(reportNotificationDTO.getSenderEmail());
        reportNotification.setText(message);
        reportNotification.setTypeStatus("согласование");
        reportNotification.setReportType(reportNotificationDTO.getReportType());
        reportNotification.setReportId(reportNotificationDTO.getReportId());
        reportNotification.setSeen(false);
        reportNotification.setSentTime(LocalDateTime.now());
        reportNotificationRepository.save(reportNotification);
        return message;
    }

    public void reportNotify(ReportNotificationDTO reportNotificationDTO) throws MessagingException, IOException, FirebaseMessagingException {
        if (reportNotificationDTO.isSenderAdmin()) {
            String body = generateReportMessageAdmin(reportNotificationDTO.getTypeStatus(), reportNotificationDTO);
            iEmailService.sendMail(reportNotificationDTO.getReceiverEmail(), generateReportSubjectAdmin(reportNotificationDTO.getTypeStatus()), body);
            sendNotificationMobile(reportNotificationDTO.getReceiverEmail(), body);
        }
        else {
            String body = generateReportMessage(reportNotificationDTO);
            iEmailService.sendMail(reportNotificationDTO.getReceiverEmail(), generateReportSubject(), generateReportMessage(reportNotificationDTO));
            sendNotificationMobile(reportNotificationDTO.getReceiverEmail(), body);
        }
    }
}
