package com.ahnis.servaia.notification.service;

import com.ahnis.servaia.analysis.dto.MoodReportEmailResponse;
import com.ahnis.servaia.notification.template.EmailTemplateService;
import com.ahnis.servaia.user.entity.Therapist;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.exception.UserNotFoundException;
import com.ahnis.servaia.user.repository.TherapistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SendGridEmailService sendGridEmailService;
    private final EmailTemplateService emailTemplateService;
    private final TherapistRepository therapistRepository;

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


    public void sendSuicidalAlert(String username, String therapistId, String concerningMessage) {
        var therapist = therapistRepository
                .findById(therapistId)
                .orElseThrow(() -> new UserNotFoundException("Therapist not found" + therapistId));

        var subject = "🚨 Urgent: Suicide Risk Alert for " + username;
        var htmlContent = emailTemplateService.generateSuicidalAlertEmail(
                therapist.getFirstName(),
                username,
                concerningMessage,
                LocalDateTime.now()
        );
        sendGridEmailService.sendEmail(therapist.getEmail(), subject, htmlContent);
    }

}
