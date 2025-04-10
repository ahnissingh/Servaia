package com.ahnis.servaia.user.repository;

import com.ahnis.servaia.user.entity.Therapist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface TherapistRepository extends MongoRepository<Therapist, String> {
    @Query("{ '$or' : [ { 'username' : ?0 }, { 'email' : ?0 } ] }")
    Optional<Therapist> findByUsernameOrEmail(String identifier);
    boolean existsByUsernameOrEmail(String username, String email);
}
