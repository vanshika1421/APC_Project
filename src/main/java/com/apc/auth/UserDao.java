package com.apc.auth;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserDao {
    public void saveUser(Session session, User user) {
        Transaction tx = session.beginTransaction();
        session.save(user);
        tx.commit();
    }

    public User getUserByUsername(Session session, String username) {
        Query<User> query = session.createQuery("from User where username = :username", User.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }
}
