package com.apc.controller;

import com.apc.purchase.PurchaseService;
import com.apc.purchase.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "*")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        try {
            List<Purchase> purchases = purchaseService.listPurchases();
            return ResponseEntity.ok(purchases);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable String id) {
        try {
            Purchase purchase = purchaseService.getPurchaseById(id);
            if (purchase != null) {
                return ResponseEntity.ok(purchase);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addPurchase(@RequestBody PurchaseRequest request) {
        try {
            purchaseService.addPurchase(
                request.getSupplier(), 
                request.getItemName(), 
                request.getQuantity(), 
                request.getPrice(),
                request.getPaymentMethod()
            );
            return ResponseEntity.ok("Purchase added successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error adding purchase: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePurchase(@PathVariable String id, @RequestBody PurchaseRequest request) {
        try {
            purchaseService.updatePurchase(id, request.getSupplier(), request.getItemName(), request.getQuantity(), request.getPrice(), request.getPaymentMethod());
            return ResponseEntity.ok("Purchase updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating purchase: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable String id) {
        try {
            purchaseService.deletePurchase(id);
            return ResponseEntity.ok("Purchase deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting purchase: " + e.getMessage());
        }
    }

    // DTO for request body
    public static class PurchaseRequest {
        private String supplier;
        private String itemName;
        private int quantity;
        private double price;
        private String paymentMethod;

        public String getSupplier() { return supplier; }
        public void setSupplier(String supplier) { this.supplier = supplier; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }
}