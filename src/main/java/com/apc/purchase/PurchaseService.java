package com.apc.purchase;

import com.apc.purchase.PurchaseDao;
import com.apc.purchase.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseDao purchaseDao;

    public void setPurchaseDao(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    @Transactional
    public void addPurchase(String supplier, String itemName, int quantity, double price) {
        Purchase purchase = new Purchase(supplier, itemName, quantity, price, new Date());
        purchaseDao.savePurchase(purchase);
    }

    @Transactional(readOnly = true)
    public List<Purchase> listPurchases() {
        return purchaseDao.getAllPurchases();
    }
}