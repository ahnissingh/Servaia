package com.ahnis.servaia.notification.service;

import com.ahnis.servaia.analysis.dto.MoodReportEmailResponse;
import com.ahnis.servaia.notification.template.EmailTemplateService;
import com.ahnis.servaia.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SendGridEmailService sendGridEmailService;
    private final EmailTemplateService emailTemplateService;
    @Async
    public void sendEmailReport(String toEmail, MoodReportEmailResponse reportContent) {
        var subject = "Your Journal Report ";
        var htmlContent = emailTemplateService.generateMoodReportEmail(reportContent);
        sendGridEmailService.sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendEmailPasswordReset(String toEmail, String token) {
        var subject = "Password request for Journal AI";
        var htmlContent = emailTemplateService.generatePasswordResetEmail(token);
        sendGridEmailService.sendEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendMilestoneNotification(User user, int streak) {
        var subject = "🎉 Congratulations on Your " + streak + "-Day Streak!";
        var htmlContent = emailTemplateService.generateMilestoneNotificationEmail(user.getUsername(), streak);
        sendGridEmailService.sendEmail(user.getEmail(), subject, htmlContent);
        log.info("Milestone notification email sent to {}", user.getEmail());
    }
}
