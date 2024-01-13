package kz.kazgeowarning.authgateway.security.service.impl;

import kz.kazgeowarning.authgateway.security.service.IEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

//    @Qualifier("mailSender")
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Value( "${spring.mail.from}" )
//    private String fromEmail;
//
//    @Override
//    public void sendMail(String toEmail, String subject, String message) throws InternalException {
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
//
//            mimeMessage.setContent(message, "text/html; charset=utf-8");
//            helper.setTo(toEmail);
//            helper.setFrom(fromEmail);
//            helper.setSubject(subject);
//
//            javaMailSender.send(mimeMessage);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void sendMail(Object toEmail) throws InternalException {
//        try {
//            System.out.println("To email" + toEmail);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void sendMailToEmails(String[] toEmails, String subject, String message) throws InternalException {
//        try {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
//
//            mimeMessage.setContent(message, "text/html; charset=utf-8");
//            helper.setBcc(toEmails);
//            helper.setFrom(fromEmail);
//            helper.setSubject(subject);
//
//            javaMailSender.send(mimeMessage);
//        } catch (Exception e) {
//            LOGGER.error("Ошибка при отправке сообщения " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public void sendMessageWithAttachment(String to, String subject, String text,
//                                          File file) throws InternalException {
//
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setFrom(fromEmail);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(text, true);
//
//            helper.addAttachment("ApplicationReceipt", file);
//
//            javaMailSender.send(message);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//        }
//    }
}
