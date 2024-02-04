package kz.geowarning.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;


@Service
public class NotificationService {
    @Autowired
    private SMTPService iEmailService;

    @Value("${application.link.verification-link}")
    private String verificationLink;

    public void notifyWarning(String warningType, String userEmail, String region, String dangerPossibility) throws MessagingException {
        iEmailService.sendMail(userEmail, generateWarningSubject(region, dangerPossibility), generateWarningMessage(userEmail, warningType, dangerPossibility));
    }

    public String generateWarningMessage(String userEmail, String warningType, String dangerPossibility){
        String message = "Уважаемый пользователь," + userEmail + "\n\n";
        message += "Мы обнаружили " + warningType + " с возможностью опасности " + dangerPossibility + "% ";
        message += "Примите немедленные меры для обеспечения безопасности.\n";
        message += "Если у вас есть какие-либо вопросы или требуется дополнительная информация, пожалуйста, ";
        message += "свяжитесь с нашей службой поддержки.\n\n\n";
        message += "С уважением,\n";
        message += "Команда kazgeowarning!\n\n";

        message += "Құрметті пайдаланушы," + userEmail + "\n\n";
        message += "Біз ескерту түрі " + warningType + " қауіп ықтималдығы " + dangerPossibility + "% жарамдықтың мүмкіндігін анықтадық. ";
        message += "Егер сізде сұрауларыңыз немесе көмек қажет болса, алдыңғы көмек қызметімен байланысуыңызды сұраймыз.\n\n";
        message += "Сіздің " + "kazgeowarning" + " қомандасы!.\n\n\n";

        message += "Dear user," + userEmail + "\n\n";
        message += "We have detected a " + warningType + " with a possibility of danger of " + dangerPossibility + "% ";
        message += "Please take immediate action to ensure safety. ";
        message += "If you have any questions or need further information, please contact our support team.\n\n";
        message += "Sincerely,\n";
        message += "The kazgeowarning team!";

        System.out.println(userEmail);
        return message;
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
        subject += "Kazgeowarning порталға қош келдіңіз, электрондық пошта мекенжайыңызды растаңыз\n";
        subject += "Welcome to kazgeowarning, please verify your email address";

        return subject;
    }
}
