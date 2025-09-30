package com.apc.purchase;


import com.apc.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private InventoryService inventoryService;

    public void setPurchaseDao(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    /**
     * Add purchase with integrated inventory management
     * This will both record the purchase and update inventory stock
     */
    @Transactional
    public void addPurchase(String supplier, String itemName, int quantity, double price) {
        // Record the purchase transaction
        Purchase purchase = new Purchase(supplier, itemName, quantity, price, new Date());
        purchaseDao.savePurchase(purchase);
        
        // Update inventory stock (increase quantity)
        inventoryService.increaseStock(itemName, quantity, price);
    }

    @Transactional(readOnly = true)
    public List<Purchase> listPurchases() {
        return purchaseDao.getAllPurchases();
    }
}