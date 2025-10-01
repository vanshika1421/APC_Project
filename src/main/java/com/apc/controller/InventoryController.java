package com.apc.controller;

import com.apc.inventory.service.InventoryService;
import com.apc.inventory.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems() {
        try {
            List<Item> items = inventoryService.listItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/items")
    public ResponseEntity<String> addItem(@RequestBody ItemRequest request) {
        try {
            inventoryService.addItem(request.getName(), request.getQuantity(), request.getPrice());
            return ResponseEntity.ok("Item added successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error adding item: " + e.getMessage());
        }
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<String> updateItem(@PathVariable String id, @RequestBody ItemRequest request) {
        try {
            inventoryService.updateItem(id, request.getQuantity(), request.getPrice());
            return ResponseEntity.ok("Item updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating item: " + e.getMessage());
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable String id) {
        try {
            System.out.println("Attempting to delete item with ID: " + id);
            inventoryService.removeItem(id);
            System.out.println("Successfully deleted item with ID: " + id);
            return ResponseEntity.ok("Item deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting item with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error deleting item: " + e.getMessage());
        }
    }

    // DTO for request body
    public static class ItemRequest {
        private String name;
        private int quantity;
        private double price;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }
}