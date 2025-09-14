package com.apc.purchase;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String supplier;
    private String itemName;
    private int quantity;
    private double price;
    private Date date;

    public Purchase() {}

    public Purchase(String supplier, String itemName, int quantity, double price, Date date) {
        this.supplier = supplier;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
