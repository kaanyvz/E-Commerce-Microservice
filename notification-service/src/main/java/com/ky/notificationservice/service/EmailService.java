package com.ky.notificationservice.service;

import com.ky.amqp.dto.EmailRequest;
import com.ky.notificationservice.exception.EmailSendingException;
import com.sun.mail.smtp.SMTPTransport;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

import static com.ky.notificationservice.constant.EmailConstant.*;
import static jakarta.mail.Message.RecipientType.CC;
import static jakarta.mail.Message.RecipientType.TO;

@Service
public class EmailService {
    Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(EmailRequest emailRequest) {
        try {
            Message message = createEmail(emailRequest);
            SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
            System.out.println(smtpTransport);
            smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();
        }catch (MessagingException e) {
            logger.error("Error sending email:", e);
            throw new EmailSendingException(e);
        }
    }

    private Message createEmail(EmailRequest emailRequest) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(emailRequest.getEmail(), false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getText());
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }
}