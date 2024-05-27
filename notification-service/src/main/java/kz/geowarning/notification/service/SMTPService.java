package kz.geowarning.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Slf4j
public class SMTPService {

    @Autowired
    private JavaMailSender emailSender;

    @Value( "${spring.mail.from}" )
    private String fromEmail;

    public synchronized void sendMail(String email, String head, String msg) throws MessagingException {
        try {
            System.out.println("SMTP STARTED");
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Added custom header and footer
            String header = "<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    "@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@500&display=swap');\n" +
                    "body { font-family: 'Montserrat', sans-serif; }\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div style='width:85%; margin-left: auto; margin-right: auto;'>\n" +
                    "<div style='text-align:center; background-color:#C8C8C8; width:100%;'>\n" +
                    "<div style='text-align: center;'>\n" +
                    "<img src='https://i.postimg.cc/tJ9FZb7P/header.png' alt='KazGeoWarning Header' style='width: 100%;'>\n" +
                    "</div>" +
                    "<div style='background-color: #E9E9E9; width: 70%; padding: 5%; margin-left: auto; margin-right: auto;'>\n" +
                    "<p style='text-align: justify !important; font-family:Montserrat Medium !important; font-size:1vw !important;'>";
            String footer = "</p>\n" +
                    "</div>" + "<div style='text-align: center;'>\n" +
                    "<img src='https://i.postimg.cc/ryW8zWLS/footer.png' alt='KazGeoWarning Footer' style='width: 100%;'>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";

            String fullMessage = header + msg + footer;

            message.setContent(fullMessage, "text/html; charset=utf-8");
            helper.setSubject(head);
            helper.setTo(email);
            helper.setFrom(fromEmail);
            emailSender.send(message);
            System.out.println("SMTP ENDED");
        } catch (MailException exception) {
            log.error("Error occurred while sending email: {}", exception.getMessage());
        }
    }
}
