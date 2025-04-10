package com.ahnis.servaia.user.repository;

import com.ahnis.servaia.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ '$or' : [ { 'username' : ?0 }, { 'email' : ?0 } ] }")
    Optional<User> findByUsernameOrEmail(String identifier);
}
