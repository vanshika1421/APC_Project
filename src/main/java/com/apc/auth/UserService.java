package com.apc.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userRepository;
    
    @Autowired
    private AuthService authService;

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Check if user exists
     */
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Create new user
     */
    public void createUser(String username, String password) {
        if (userExists(username)) {
            throw new RuntimeException("User already exists: " + username);
        }
        authService.register(username, password);
    }

    /**
     * Update user password
     */
    public void updateUserPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        // Use AuthService to hash the password properly
        user.setPassword(authService.hashPassword(newPassword));
        userRepository.save(user);
    }

    /**
     * Delete user
     */
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    /**
     * Get user count
     */
    public long getUserCount() {
        return userRepository.count();
    }
}