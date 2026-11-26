package com.polycoffee.util;

import com.polycoffee.exception.BusinessRuleException;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/** Gửi mail SMTP. Cấu hình ở mail.properties (không commit) hoặc biến môi trường MAIL_*. */
public class MailUtil implements MailSender {

    @Override
    public void send(String to, String subject, String body) {
        Properties config = ConfigUtil.load("mail.properties");
        String host = ConfigUtil.get(config, "MAIL_HOST", "mail.host", "");
        if (host.isBlank()) {
            throw new BusinessRuleException("Chưa cấu hình máy chủ gửi mail.");
        }
        String user = ConfigUtil.get(config, "MAIL_USER", "mail.user", "");
        String password = ConfigUtil.get(config, "MAIL_PASSWORD", "mail.password", "");
        String from = ConfigUtil.get(config, "MAIL_FROM", "mail.from", user);

        Properties smtp = new Properties();
        smtp.put("mail.smtp.host", host);
        smtp.put("mail.smtp.port", ConfigUtil.get(config, "MAIL_PORT", "mail.port", "587"));
        smtp.put("mail.smtp.auth", String.valueOf(!user.isBlank()));
        smtp.put("mail.smtp.starttls.enable", ConfigUtil.get(config, "MAIL_STARTTLS", "mail.starttls", "true"));
        smtp.put("mail.smtp.connectiontimeout", "5000");
        smtp.put("mail.smtp.timeout", "5000");

        Session session = user.isBlank() ? Session.getInstance(smtp) : Session.getInstance(smtp, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject, "UTF-8");
            message.setText(body, "UTF-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new BusinessRuleException("Không gửi được email. Vui lòng thử lại sau.");
        }
    }
}
