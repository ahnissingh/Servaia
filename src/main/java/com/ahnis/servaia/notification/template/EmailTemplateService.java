package com.ahnis.servaia.notification.template;


import com.ahnis.servaia.common.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateService {
    private final SpringTemplateEngine templateEngine;
    private final AppProperties appProperties;
//
//    public String generateMoodReportEmail(MoodReportEmailResponse report) {
//        var context = new Context();
//        context.setVariable("report", report);
//        return templateEngine.process("email/mood-report-email", context);
//    }

    public String generatePasswordResetEmail(String token) {
        var context = new Context();
        var resultUrl = appProperties.getBaseUrl() +
                appProperties.getPasswordResetUrl() +
                "?token=" +
                token;
        log.info("token for password reset email in Email Template service {}", token);

        context.setVariable("resetUrl", resultUrl);
        return templateEngine.process("email/password-reset-email", context);

    }
    //
//    public String generateJournalReminderEmail() {
//        var context = new Context();
//        var journalUrl = appProperties.getBaseUrl() + "/journal";
//        context.setVariable("journalUrl", journalUrl);
//        return templateEngine.process("email/journal-reminder-email", context);
//    }
//
//    public String generateMilestoneNotificationEmail(String userName, int streak) {
//        var context = new Context();
//        var journalUrl = appProperties.getBaseUrl() + "/journal";
//        context.setVariable("userName", userName);
//        context.setVariable("streak", streak);
//        context.setVariable("journalUrl", journalUrl);
//        return templateEngine.process("email/milestone-notification-email", context);
//    }

}

