package com.ahnis.servaia.user.repository;

import com.ahnis.servaia.user.entity.Preferences;
import com.ahnis.servaia.user.entity.User;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ '$or' : [ { 'username' : ?0 }, { 'email' : ?0 } ] }")
    Optional<User> findByUsernameOrEmail(String identifier);

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'preferences' : ?1 } }")
    void updatePreferences(String userId, Preferences preferences);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'enabled' : ?1 } }")
    long updateEnabledStatus(String userId, boolean enabled);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'accountNonLocked' : ?1 } }")
    long updateAccountNonLockedStatus(String userId, boolean accountNonLocked);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'email' : ?1 } }")
    long updateEmail(String userId, String email);

    @Query("{ '_id' : ?0 }")
    @Transactional
    @Update("{ '$set' : { 'password' : ?1 } }")
    long updatePassword(String userId, String password);


    @Query("{ 'username' : ?0 }")
    @Update("{ '$set' : { 'password' : ?1 } }")
    long updatePasswordByUsername(String username, String password);

    @Query("{ 'username' : ?0 }")
    @Update("{ '$set' : { 'email' : ?1 } }")
    long updateEmailByUsername(String username, String email);

    @Query("{ 'nextReportOn' : { $gte: ?0, $lte: ?1 } }")
    List<User> findByNextReportOn(Instant startOfDay, Instant endOfDay);

    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'nextReportOn' : ?1 } }")
    void updateNextReportOnById(String userId, Instant nextReportOn);

    @Transactional
    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'lastReportAt' : ?1 } }")
    void updateLastReportAtById(String userId, Instant lastReportAt);

    @Transactional
    @Query("{ 'username' : ?0 }")
    @Update("{ '$set' : { 'nextReportOn' : ?1 } }")
    void updateByUsernameAndNextReportOn(String username, Instant nextReportOn);

    @Query("{ 'username' : ?0 }")
    @DeleteQuery
    long deleteByUsername(String username);

    @Query("{ 'username' : ?0 }")
    @Update("{ '$set' : { 'preferences' : ?1 } }")
    long updatePreferencesByUsername(String username, Preferences preferences);


    @Query("{ 'username' : ?0 }")
    @Update("{ '$set' : { 'nextReportOn' : ?1 } }")
    long updateNextReportOnByUsername(String username, Instant nextReportOn);


    @Query("{ 'preferences.remindersEnabled': ?0 }")
    List<User> findByRemindersEnabled(boolean remindersEnabled);
}
