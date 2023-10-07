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

    public void sendMail(String email, String head, String msg) throws MessagingException {
        try {
            System.out.println("SMTP STARTED");
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setContent(msg, "text/html; charset=utf-8");
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
