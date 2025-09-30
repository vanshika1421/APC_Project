package com.apc.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private AuthService authService;

    /**
     * Get all users
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    /**
     * Get user by username
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    /**
     * Check if user exists
     */
    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        return userDao.getUserByUsername(username) != null;
    }

    /**
     * Create new user
     */
    @Transactional
    public void createUser(String username, String password) {
        if (userExists(username)) {
            throw new RuntimeException("User already exists: " + username);
        }
        authService.register(username, password);
    }

    /**
     * Update user password
     */
    @Transactional
    public void updateUserPassword(String username, String newPassword) {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        // Use AuthService to hash the password properly
        user.setPassword(authService.hashPassword(newPassword));
        userDao.updateUser(user);
    }

    /**
     * Delete user
     */
    @Transactional
    public void deleteUser(String username) {
        User user = userDao.getUserByUsername(username);
        if (user != null) {
            userDao.deleteUser(user);
        }
    }

    /**
     * Get user count
     */
    @Transactional(readOnly = true)
    public long getUserCount() {
        return userDao.getUserCount();
    }
}