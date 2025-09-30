package com.apc.config;

import com.apc.auth.UserService;
import com.apc.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeInventory();
    }

    private void initializeUsers() {
        // Create default users if none exist
        if (userService.getUserCount() == 0) {
            System.out.println("Creating default users...");
            
            // Create admin user
            userService.createUser("admin", "admin123");
            System.out.println("Created admin user: admin/admin123");
            
            // Create demo user
            userService.createUser("vanshika", "password");
            System.out.println("Created demo user: vanshika/password");
            
            // Create test user
            userService.createUser("test", "test123");
            System.out.println("Created test user: test/test123");
            
            System.out.println("Default users created successfully!");
        } else {
            System.out.println("Users already exist in database. Current user count: " + userService.getUserCount());
        }
    }

    private void initializeInventory() {
        // Add some sample inventory items if inventory is empty
        if (inventoryService.listItems().isEmpty()) {
            System.out.println("Creating sample inventory items...");
            
            // Add sample items
            inventoryService.addItem("Apple", 100, 1.50);
            inventoryService.addItem("Banana", 150, 0.75);
            inventoryService.addItem("Orange", 80, 2.00);
            inventoryService.addItem("Milk", 50, 3.25);
            inventoryService.addItem("Bread", 30, 2.50);
            inventoryService.addItem("Eggs", 60, 4.00);
            inventoryService.addItem("Chicken", 25, 8.99);
            inventoryService.addItem("Rice", 200, 5.50);
            
            System.out.println("Sample inventory items created successfully!");
        } else {
            System.out.println("Inventory already has items. Current item count: " + inventoryService.listItems().size());
        }
    }
}