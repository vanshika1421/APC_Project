package com.apc.purchase;

import org.hibernate.Session;
import java.util.Date;
import java.util.List;

public class PurchaseService {
    private final PurchaseDao purchaseDao = new PurchaseDao();

    public void addPurchase(Session session, String supplier, String itemName, int quantity, double price) {
        Purchase purchase = new Purchase(supplier, itemName, quantity, price, new Date());
        purchaseDao.savePurchase(session, purchase);
    }

    public List<Purchase> listPurchases(Session session) {
        return purchaseDao.getAllPurchases(session);
    }
}
