package com.apc.auth;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public boolean authenticate(String username, String password) {
        User user = userDao.findByUsername(username).orElse(null);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public void register(String username, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, hashedPassword);
        userDao.save(user);
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}