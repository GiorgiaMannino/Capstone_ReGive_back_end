package it.epicode.Capstone_ReGive_back_end.mails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String[] recipients = to.split("\\s*[,;]\\s*");
        helper.setTo(recipients);

        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
        System.out.println("Email inviata con successo a " + to);
    }

    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachmentBytes, String attachmentName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String[] recipients = to.split("\\s*[,;]\\s*");
        helper.setTo(recipients);

        helper.setSubject(subject);
        helper.setText(body, true);
        helper.addAttachment(attachmentName, new ByteArrayResource(attachmentBytes));
        mailSender.send(message);
        System.out.println("Email inviata con successo a " + to + " con allegato: " + attachmentName);
    }
}