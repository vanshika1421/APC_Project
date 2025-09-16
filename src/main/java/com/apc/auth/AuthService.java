
package com.apc.auth;

import org.springframework.stereotype.Component;

import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AuthService {
    @Autowired
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean authenticate(Session session, String username, String password) {
        User user = userDao.getUserByUsername(session, username);
        return user != null && user.getPassword().equals(password);
    }

    public void register(Session session, String username, String password) {
        User user = new User(username, password);
        userDao.saveUser(session, user);
    }
}
