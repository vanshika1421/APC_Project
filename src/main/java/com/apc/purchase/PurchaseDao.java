package com.apc.purchase;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class PurchaseDao {
    public void savePurchase(Session session, Purchase purchase) {
        Transaction tx = session.beginTransaction();
        session.save(purchase);
        tx.commit();
    }

    public List<Purchase> getAllPurchases(Session session) {
        Query<Purchase> query = session.createQuery("from Purchase", Purchase.class);
        return query.list();
    }
}
