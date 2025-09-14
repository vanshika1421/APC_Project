package com.apc;

import com.apc.config.HibernateUtil;
import com.apc.inventory.service.InventoryService;
import com.apc.billing.service.BillingService;
import com.apc.auth.AuthService;
import org.hibernate.Session;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Session session = HibernateUtil.getSessionFactory().openSession();
        AuthService authService = new AuthService();
        InventoryService inventoryService = new InventoryService();
        BillingService billingService = new BillingService();

        boolean authenticated = false;
        while (!authenticated) {
            System.out.println("Welcome to Inventory & Billing System");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    if (authService.authenticate(session, username, password)) {
                        System.out.println("Login successful!");
                        authenticated = true;
                    } else {
                        System.out.println("Invalid credentials. Try again.");
                    }
                    break;
                case 2:
                    System.out.print("Choose a username: ");
                    String newUser = scanner.nextLine();
                    System.out.print("Choose a password: ");
                    String newPass = scanner.nextLine();
                    if (authService.authenticate(session, newUser, newPass)) {
                        System.out.println("User already exists! Please login.");
                    } else {
                        authService.register(session, newUser, newPass);
                        System.out.println("Registration successful!");
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    session.close();
                    HibernateUtil.shutdown();
                    System.exit(0);
                default:
                    System.out.println("Invalid option.");
            }
        }

        boolean running = true;
        while (running) {
            System.out.println("\nMain Menu");
            System.out.println("1. Inventory Management");
            System.out.println("2. Billing System");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int mainChoice = scanner.nextInt();
            scanner.nextLine();
            switch (mainChoice) {
                case 1:
                    boolean invMenu = true;
                    while (invMenu) {
                        System.out.println("\nInventory Menu");
                        System.out.println("1. Add Item");
                        System.out.println("2. View Items");
                        System.out.println("3. Update Item");
                        System.out.println("4. Delete Item");
                        System.out.println("5. Back");
                        System.out.print("Choose an option: ");
                        int invChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (invChoice) {
                            case 1:
                                System.out.print("Item name: ");
                                String name = scanner.nextLine();
                                System.out.print("Quantity: ");
                                int qty = scanner.nextInt();
                                System.out.print("Price: ");
                                double price = scanner.nextDouble();
                                scanner.nextLine();
                                inventoryService.addItem(session, name, qty, price);
                                System.out.println("Item added.");
                                break;
                            case 2:
                                System.out.println("Items:");
                                inventoryService.listItems(session).forEach(item ->
                                    System.out.println(item.getId() + ": " + item.getName() + ", Qty: " + item.getQuantity() + ", Price: " + item.getPrice())
                                );
                                break;
                            case 3:
                                System.out.print("Item ID to update: ");
                                int upId = scanner.nextInt();
                                System.out.print("New quantity: ");
                                int upQty = scanner.nextInt();
                                System.out.print("New price: ");
                                double upPrice = scanner.nextDouble();
                                scanner.nextLine();
                                inventoryService.updateItem(session, upId, upQty, upPrice);
                                System.out.println("Item updated.");
                                break;
                            case 4:
                                System.out.print("Item ID to delete: ");
                                int delId = scanner.nextInt();
                                scanner.nextLine();
                                inventoryService.removeItem(session, delId);
                                System.out.println("Item deleted.");
                                break;
                            case 5:
                                invMenu = false;
                                break;
                            default:
                                System.out.println("Invalid option.");
                        }
                    }
                    break;
                case 2:
                    boolean billMenu = true;
                    while (billMenu) {
                        System.out.println("\nBilling Menu");
                        System.out.println("1. Create Invoice");
                        System.out.println("2. View Invoices");
                        System.out.println("3. Update Invoice");
                        System.out.println("4. Delete Invoice");
                        System.out.println("5. Back");
                        System.out.print("Choose an option: ");
                        int billChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (billChoice) {
                            case 1:
                                System.out.print("Enter item ID or name: ");
                                String itemInput = scanner.nextLine();
                                com.apc.inventory.model.Item item = null;
                                try {
                                    int itemId = Integer.parseInt(itemInput);
                                    item = inventoryService.listItems(session).stream()
                                        .filter(i -> i.getId() == itemId)
                                        .findFirst().orElse(null);
                                } catch (NumberFormatException e) {
                                    item = inventoryService.listItems(session).stream()
                                        .filter(i -> i.getName().equalsIgnoreCase(itemInput))
                                        .findFirst().orElse(null);
                                }
                                if (item == null) {
                                    System.out.println("Item not found in inventory.");
                                    break;
                                }
                                System.out.print("Enter quantity: ");
                                int billQty = scanner.nextInt();
                                scanner.nextLine();
                                double baseTotal = billQty * item.getPrice();
                                double gstRate = 0.18; // 18% GST
                                double gstAmount = baseTotal * gstRate;
                                double finalTotal = baseTotal + gstAmount;
                                System.out.printf("Base Total: %.2f\nGST (18%%): %.2f\nFinal Amount: %.2f\n",
                                    baseTotal, gstAmount, finalTotal);
                                if (item.getQuantity() < billQty) {
                                    System.out.println("Not enough quantity in inventory. Available: " + item.getQuantity());
                                    break;
                                }
                                // Deduct quantity from inventory
                                inventoryService.updateItem(session, item.getId(), item.getQuantity() - billQty, item.getPrice());
                                billingService.createInvoice(session, finalTotal);
                                System.out.println("Invoice created for item: " + item.getName());
                                break;
                            case 2:
                                System.out.println("Invoices:");
                                billingService.listInvoices(session).forEach(inv ->
                                    System.out.println(inv.getId() + ": Amount: " + inv.getTotalAmount() + ", Date: " + inv.getDate())
                                );
                                break;
                            case 3:
                                System.out.print("Invoice ID to update: ");
                                int upInvId = scanner.nextInt();
                                System.out.print("New amount: ");
                                double upAmt = scanner.nextDouble();
                                scanner.nextLine();
                                billingService.updateInvoice(session, upInvId, upAmt);
                                System.out.println("Invoice updated.");
                                break;
                            case 4:
                                System.out.print("Invoice ID to delete: ");
                                int delInvId = scanner.nextInt();
                                scanner.nextLine();
                                billingService.removeInvoice(session, delInvId);
                                System.out.println("Invoice deleted.");
                                break;
                            case 5:
                                billMenu = false;
                                break;
                            default:
                                System.out.println("Invalid option.");
                        }
                    }
                    break;
                case 3:
                    System.out.println("Logging out...");
                    authenticated = false;
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        session.close();
        HibernateUtil.shutdown();
    }
}
