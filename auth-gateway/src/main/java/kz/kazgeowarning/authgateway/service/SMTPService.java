package kz.kazgeowarning.authgateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class SMTPService {

    @Autowired
    private JavaMailSender emailSender;

    @Value( "${spring.mail.from}" )
    private String fromEmail;

    public void sendMail(String email, String head, String msg) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        message.setContent(msg, "text/html; charset=utf-8");
        helper.setSubject(head);
        helper.setTo(email);
        helper.setFrom(fromEmail);
        try {
            emailSender.send(message);
        } catch (MailException exception) {
            exception.getMessage();
        }
    }
}
