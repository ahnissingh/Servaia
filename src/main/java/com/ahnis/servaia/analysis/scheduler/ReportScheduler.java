package com.ahnis.servaia.analysis.scheduler;

import com.ahnis.servaia.analysis.service.ReportService;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static com.ahnis.servaia.user.util.UserUtils.calculateNextReportOn;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReportScheduler {
    private final UserRepository userRepository;
    private final ReportService reportService;

    //todo next asap PROFILING dev and prod
    //todo in prod and dev have cron expression in yaml

    @Scheduled(cron = "${scheduler.check-reports.cron}", zone = "${scheduler.check-reports.zone}")
    public void checkForReports() {
        log.info("Scheduler running {} and is virtual : {} ", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        // Get the current date in UTC
        ZonedDateTime nowInUTC = ZonedDateTime.now(ZoneOffset.UTC);
        LocalDate todayInUTC = nowInUTC.toLocalDate();

        // Convert today's date to the start of the day in UTC
        Instant startOfDayInUTC = todayInUTC.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDayInUTC = todayInUTC.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        log.info("Checking for reports due between: {} and {} (UTC)", startOfDayInUTC, endOfDayInUTC);

        // Fetch users who have nextReportAt equal to today
        List<User> usersDueToday = userRepository.findByNextReportOn(startOfDayInUTC, endOfDayInUTC);
        log.info("Users have report today: {}", usersDueToday);

        if (usersDueToday.isEmpty()) {
            log.info("No users found with reports due today.");
            return;
        }

        usersDueToday.forEach(user -> {
            try {
                Instant lastReportAt = user.getLastReportAt();
                Instant nextReportOn = user.getNextReportOn();

                Instant newNextReportOn = calculateNextReportOn(nextReportOn, user.getPreferences().getReportFrequency());
                //EDGE CASES
                //Existing user with last report generated
                var startDate = (lastReportAt != null) ? lastReportAt : user.getCreatedAt();
                reportService.generateReport(user, startDate, nextReportOn);
                log.info("Report generated and dates updated for user: {}", user.getUsername());
                //update last reportAt to today(nextReportOn)
                userRepository.updateLastReportAtById(user.getId(), nextReportOn);
                userRepository.updateNextReportOnById(user.getId(), newNextReportOn);
                log.info("LastReportAt and NextReportAt fields updated for user {} ", user.getUsername());

            } catch (Exception e) {
                log.error("Failed to generate report for user: {}", user.getUsername(), e);
            }
        });
    }
}
