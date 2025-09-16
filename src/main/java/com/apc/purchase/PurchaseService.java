
package com.apc.purchase;

import org.springframework.stereotype.Component;

import org.hibernate.Session;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class PurchaseService {
    @Autowired
    private PurchaseDao purchaseDao;

    public void setPurchaseDao(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    public void addPurchase(Session session, String supplier, String itemName, int quantity, double price) {
        Purchase purchase = new Purchase(supplier, itemName, quantity, price, new Date());
        purchaseDao.savePurchase(session, purchase);
    }

    public List<Purchase> listPurchases(Session session) {
        return purchaseDao.getAllPurchases(session);
    }
}
