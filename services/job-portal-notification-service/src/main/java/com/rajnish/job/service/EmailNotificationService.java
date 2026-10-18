package com.rajnish.job.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


    public void sendStatusChangedEmail() throws Exception {
        try {
            String subject = "Application update: ";
            String body = buildStatusChangedHtml();

            String candidateEmail = "rajnish.kum41@gmail.com";
            sendEmail(candidateEmail, subject, body);

        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private void sendEmail(String candidateEmail, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(candidateEmail);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(mimeMessage);
    }

    private String buildStatusChangedHtml(){
        return "<h1>Application status changed</h1>";
    }


}
