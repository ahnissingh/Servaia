package com.ahnis.servaia.journal.service;

import com.ahnis.servaia.journal.dto.request.JournalRequest;
import com.ahnis.servaia.journal.dto.response.JournalResponse;
import com.ahnis.servaia.journal.embedding.JournalEmbeddingService;
import com.ahnis.servaia.journal.entity.Journal;
import com.ahnis.servaia.journal.exception.JournalNotFoundException;
import com.ahnis.servaia.journal.mapper.JournalMapper;
import com.ahnis.servaia.journal.repository.JournalRepository;
import com.ahnis.servaia.notification.service.NotificationService;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.exception.UserNotFoundException;
import com.ahnis.servaia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {
    private final JournalRepository journalRepository;
    private final JournalEmbeddingService journalEmbeddingService;
    private final JournalMapper journalMapper;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    @Async
    public void createJournal(JournalRequest dto, String userId) { //v2
        // Map DTO to entity
        Journal journal = journalMapper.toEntity(dto, userId);

        // Save the journal and generate embeddings concurrently
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Submit tasks for concurrent execution
            CompletableFuture<Journal> saveJournalFuture = CompletableFuture.supplyAsync(
                    () -> journalRepository.save(journal), executor);

            CompletableFuture<Void> saveEmbeddingsFuture = saveJournalFuture.thenAcceptAsync(
                    savedJournal -> {
                        journalEmbeddingService.saveJournalEmbeddings(savedJournal);
                        updateUsersStreak(userId);
                    }, executor);
            // Wait for both tasks to complete
            CompletableFuture.allOf(saveJournalFuture, saveEmbeddingsFuture).join();
        } catch (Exception e) {
            log.error("Failed to create journal: {}", e.getMessage());
        }
    }

    @Override
    public List<JournalResponse> getAllJournals(String userId) {
        return journalRepository.findByUserId(userId)
                .stream()
                .map(journalMapper::toDto)
                .toList();
    }

    @Override
    public JournalResponse getJournalById(String id, String userId) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new JournalNotFoundException("Journal not found"));
        validateJournalOwnership(journal, userId);
        return journalMapper.toDto(journal);
    }

    @Override
    public JournalResponse updateJournal(String id, JournalRequest dto, String userId) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new JournalNotFoundException("Journal not found"));
        validateJournalOwnership(journal, userId);

        journal.setTitle(dto.title());
        journal.setContent(dto.content());
        var updatedJournal = journalRepository.save(journal);
        journalEmbeddingService.updateJournalEmbeddings(updatedJournal);
        return journalMapper.toDto(updatedJournal);
    }

    @Override
    public void deleteJournal(String id, String userId) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new JournalNotFoundException("Journal not found"));
        validateJournalOwnership(journal, userId);
        journalRepository.delete(journal);
        journalEmbeddingService.deleteJournalEmbeddings(id);

    }


    private void validateJournalOwnership(Journal journal, String userId) {
        if (!journal.getUserId().equals(userId))
            throw new JournalNotFoundException("Journal not found");
    }


    private void updateUsersStreak(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found" + userId));
        Instant now = Instant.now();
        Instant lastJournalEntryDate = user.getLastJournalEntryDate();
        if (lastJournalEntryDate != null && isYesterday(lastJournalEntryDate, now)) {
            user.setCurrentStreak(user.getCurrentStreak() + 1);

        } else {
            user.setCurrentStreak(1);
        }
        user.setLastJournalEntryDate(now);
        // Update longestStreak if currentStreak exceeds it
        if (user.getCurrentStreak() > user.getLongestStreak()) {
            user.setLongestStreak(user.getCurrentStreak());
        }
        checkForMilestones(user);
        userRepository.save(user);
    }

    private boolean isYesterday(Instant lastEntryDate, Instant now) {
        LocalDate lastEntryLocalDate = lastEntryDate.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate nowLocalDate = now.atZone(ZoneId.systemDefault()).toLocalDate();
        return lastEntryLocalDate.plusDays(1).isEqual(nowLocalDate);
    }

    private void checkForMilestones(User user) {
        int currentStreak = user.getCurrentStreak();
        if (currentStreak == 2 || currentStreak == 3 || currentStreak == 7 || currentStreak == 14 || currentStreak == 30) {
            sendMilestoneNotification(user, currentStreak);
        }
    }

    private void sendMilestoneNotification(User user, int streak) {
        // Log the milestone (replace with actual notification logic)
        notificationService.sendMilestoneNotification(user, streak);
        log.info("Congratulations! User {} has reached a {}-day streak!", user.getEmail(), streak);
    }

}
