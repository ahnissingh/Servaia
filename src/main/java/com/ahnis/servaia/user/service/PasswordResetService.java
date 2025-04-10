package com.ahnis.servaia.user.service;


import com.ahnis.servaia.notification.service.NotificationService;
import com.ahnis.servaia.user.entity.PasswordResetToken;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.exception.UserNotFoundException;
import com.ahnis.servaia.user.repository.PasswordResetTokenRepository;
import com.ahnis.servaia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    // Generate a token and send an email
    public void sendPasswordResetEmail(String userEmail) {
        User user = userRepository.findByUsernameOrEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a token
        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plus(60, ChronoUnit.MINUTES); // Token expires in 60 minutes

        // Save the token
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .userId(user.getId()) //Field annotation used automatically saved as object id
                .expiryDate(expiryDate)
                .build();
        passwordResetTokenRepository.save(resetToken);

        // Send the email
        notificationService.sendEmailPasswordReset(userEmail, token);
    }


    // Reset the password
    public void resetPassword(String token, String newPassword) {
        //validate the token and ownership
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));

        if (resetToken.getExpiryDate().isBefore(Instant.now()))
            throw new RuntimeException("Invalid or expired token");

        if (!userRepository.existsById(resetToken.getUserId()))
            throw new UserNotFoundException("User not found" + resetToken.getUserId());

        // Update the password
        var updateCount = userRepository.updatePassword(resetToken.getUserId(), passwordEncoder.encode(newPassword));

        // Delete the token
        if (updateCount == 0)
            throw new RuntimeException("Exception while reseting password for user id" + resetToken.getUserId());
        passwordResetTokenRepository.delete(resetToken);
    }
}
