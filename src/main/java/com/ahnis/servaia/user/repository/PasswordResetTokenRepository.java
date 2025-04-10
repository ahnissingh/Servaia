package com.ahnis.servaia.user.repository;

import com.ahnis.servaia.user.entity.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUserId(String userId);
}
