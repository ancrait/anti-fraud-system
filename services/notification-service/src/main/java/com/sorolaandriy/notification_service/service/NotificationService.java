package com.sorolaandriy.notification_service.service;

import com.sorolaandriy.notification_service.dto.TransactionalStatus;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public NotificationService(JavaMailSender mailSender, TemplateEngine templateEngine, StringRedisTemplate redisTemplate) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.redisTemplate = redisTemplate;
    }

    public void sendTransactionConfirmation(String transactionId, String toEmail, String userName, String surname,
                                            double amount, String token) {

        redisTemplate.opsForValue().set(token,transactionId,15, TimeUnit.MINUTES);

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
            context.setVariable("confirmationUrl", "http://localhost:8085/api/verify/approve?token=" + token);
            context.setVariable("rejectUrl", "http://localhost:8085/api/verify/reject?token=" + token);


            String htmlContent = templateEngine.process("transaction-confirmation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
