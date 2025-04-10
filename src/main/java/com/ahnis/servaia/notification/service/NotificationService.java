package com.ahnis.servaia.notification.service;

import com.ahnis.servaia.notification.template.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SendGridEmailService sendGridEmailService;
    private final EmailTemplateService emailTemplateService;
    @Async
    public void sendEmailPasswordReset (String toEmail, String token){
        var subject = "Password request for Journal AI";
        var htmlContent = emailTemplateService.generatePasswordResetEmail(token);
        sendGridEmailService.sendEmail(toEmail, subject, htmlContent);
    }
}
