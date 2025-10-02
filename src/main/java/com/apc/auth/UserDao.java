package com.apc.auth;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserDao extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    // MongoRepository already provides findAll() and count() methods
}
