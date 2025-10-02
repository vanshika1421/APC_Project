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
    public void addPurchase(String supplier, String itemName, int quantity, double price, String paymentMethod) {
        // Record the purchase transaction
        Purchase purchase = new Purchase(supplier, itemName, quantity, price, new Date(), paymentMethod);
        purchaseDao.savePurchase(purchase);
        
        // Update inventory stock (increase quantity)
        inventoryService.increaseStock(itemName, quantity, price);
    }

    @Transactional(readOnly = true)
    public List<Purchase> listPurchases() {
        return purchaseDao.getAllPurchases();
    }

    @Transactional(readOnly = true)
    public Purchase getPurchaseById(String id) {
        return purchaseDao.getPurchaseById(id);
    }

    @Transactional
    public void updatePurchase(String id, String supplier, String itemName, int quantity, double price, String paymentMethod) {
        Purchase purchase = purchaseDao.getPurchaseById(id);
        if (purchase != null) {
            // Calculate inventory difference
            int oldQuantity = purchase.getQuantity();
            
            // Update purchase details
            purchase.setSupplier(supplier);
            purchase.setItemName(itemName);
            purchase.setQuantity(quantity);
            purchase.setPrice(price);
            purchase.setPaymentMethod(paymentMethod);
            purchase.setDate(new Date());
            
            purchaseDao.updatePurchase(purchase);
            
            // Update inventory: subtract old quantity and add new quantity
            inventoryService.reduceStock(purchase.getItemName(), oldQuantity);
            inventoryService.increaseStock(itemName, quantity, price);
        }
    }

    @Transactional
    public void deletePurchase(String id) {
        Purchase purchase = purchaseDao.getPurchaseById(id);
        if (purchase != null) {
            // Decrease inventory stock when purchase is deleted
            inventoryService.reduceStock(purchase.getItemName(), purchase.getQuantity());
            purchaseDao.deletePurchase(purchase);
        }
    }
}