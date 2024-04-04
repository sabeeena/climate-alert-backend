package kz.geowarning.notification.service;

import kz.geowarning.notification.dto.ForecastNotificationContentDTO;
import kz.geowarning.notification.dto.RealTimeNotificationContentDTO;
import kz.geowarning.notification.dto.ReportNotificationDTO;
import kz.geowarning.notification.entity.AlertNotification;
import kz.geowarning.notification.entity.ReportNotification;
import kz.geowarning.notification.repository.AlertNotificationRepository;
import kz.geowarning.notification.repository.ReportNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


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

    public void notifyWarning(String warningType, String userEmail, String region, String dangerPossibility) throws MessagingException {
        iEmailService.sendMail(userEmail, generateWarningSubject(region, dangerPossibility), generateWarningMessage(region, userEmail, warningType, dangerPossibility));
    }

    public void notifyRealtime(RealTimeNotificationContentDTO contentDTO) throws MessagingException {
        iEmailService.sendMail(contentDTO.getEmail(), generateWarningSubjectRealtime(contentDTO), generateWarningMessageRealTime(contentDTO));
    }

    public void notifyForecast(ForecastNotificationContentDTO contentDTO) throws MessagingException {
        iEmailService.sendMail(contentDTO.getEmail(), generateWarningSubjectForecast(contentDTO), generateWarningMessageForecast(contentDTO));
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

        AlertNotification alertNotification = new AlertNotification();
        alertNotification.setReceiverEmail(contentDTO.getEmail());
        alertNotification.setSenderEmail("KazGeoWarning");
        alertNotification.setWarningType("real-time fire");
        alertNotification.setText(message);
        alertNotification.setSeen(false);
        alertNotificationRepository.save(alertNotification);
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
        message += "В пределах региона <b>" + contentDTO.getLocationName() + "</b> был обнаружен уровень опасности: <b>" + contentDTO.getLevel() + "</b>.<br><br><br>";
        message += "Если у вас есть какие-либо вопросы или требуется дополнительная информация, пожалуйста, ";
        message += "свяжитесь с нашей службой поддержки.<br><br>";
        message += "С уважением,<br>";
        message += "Команда <b>KazGeoWarning!</b><br><br>";
        message += "</span>";

        AlertNotification alertNotification = new AlertNotification();
        alertNotification.setReceiverEmail(contentDTO.getEmail());
        alertNotification.setSenderEmail("KazGeoWarning");
        alertNotification.setWarningType("forecast fire");
        alertNotification.setText(message);
        alertNotification.setSeen(false);
        alertNotificationRepository.save(alertNotification);
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
        reportNotificationRepository.save(reportNotification);
        return message;
    }

    private String generateReportSubjectAdmin(String type) {
        String message = "empty something is wrong";

        if(Objects.equals(type, "Согласовать")){
            return "Успешное согласование репорта.\n";
        } else if(Objects.equals(type, "Корректировка")) {
            return "Возврат на корректировку репорта.\n";
        }

        return message;
    }

    public String generateReportMessageAdmin(String type, ReportNotificationDTO reportNotificationDTO){
        String message = "empty something is wrong";

        if(Objects.equals(type, "Согласовать")){
            message = "Хочу сообщить, что отчет был успешно согласован от имени администратора.\n";
        } else if(Objects.equals(type, "Корректировка")) {
            message = "Хочу сообщить, что отчет был отправлен на корректировку.\n";
        }
        ReportNotification reportNotification = new ReportNotification();
        reportNotification.setReceiverEmail(reportNotificationDTO.getReceiverEmail());
        reportNotification.setSenderEmail(reportNotificationDTO.getSenderEmail());
        reportNotification.setText(message);
        reportNotification.setTypeStatus("согласование");
        reportNotification.setReportType(reportNotificationDTO.getReportType());
        reportNotification.setReportId(reportNotificationDTO.getReportId());
        reportNotification.setSeen(false);
        reportNotificationRepository.save(reportNotification);
        return message;
    }

    public void reportNotify(ReportNotificationDTO reportNotificationDTO) throws MessagingException {
        if (reportNotificationDTO.isSenderAdmin()) {
            iEmailService.sendMail(reportNotificationDTO.getReceiverEmail(), generateReportSubjectAdmin(reportNotificationDTO.getTypeStatus()), generateReportMessageAdmin(reportNotificationDTO.getTypeStatus(), reportNotificationDTO));
        }
        else {
            iEmailService.sendMail(reportNotificationDTO.getReceiverEmail(), generateReportSubject(), generateReportMessage(reportNotificationDTO));
        }
    }
}
