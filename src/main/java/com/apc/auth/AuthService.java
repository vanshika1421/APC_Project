package com.apc.auth;

import org.hibernate.Session;

public class AuthService {
    private final UserDao userDao = new UserDao();

    public boolean authenticate(Session session, String username, String password) {
        User user = userDao.getUserByUsername(session, username);
        return user != null && user.getPassword().equals(password);
    }

    public void register(Session session, String username, String password) {
        User user = new User(username, password);
        userDao.saveUser(session, user);
    }
}
