package com.ahnis.servaia.journal.repository;

import com.ahnis.servaia.journal.entity.Journal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface JournalRepository extends MongoRepository<Journal, String> {
    List<Journal> findByUserId(String userId);

    Optional<JournalCreatedAtProjection> findFirstByUserIdOrderByCreatedAtAsc(String userId);

}

