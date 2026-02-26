package com.sorolaandriy.notification_service.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public NotificationService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendTransactionConfirmation(String toEmail, String userName, String surname, double amount, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


            helper.setFrom(fromEmail, "AntiFraud Security System");
            helper.setTo(toEmail);
            helper.setSubject("Підтвердження операції - " + amount + " грн");


            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("surname", surname);
            context.setVariable("amount", amount);
            context.setVariable("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            context.setVariable("transactionId", token);
            context.setVariable("confirmationUrl", "http://localhost:8083/api/v1/verify?token=" + token);


            String htmlContent = templateEngine.process("transaction-confirmation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
