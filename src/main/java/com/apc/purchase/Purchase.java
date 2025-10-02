package com.apc.purchase;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String supplier;
    private String itemName;
    private int quantity;
    private double price;
    private Date date;
    private String paymentMethod;

    public Purchase() {}

    public Purchase(String supplier, String itemName, int quantity, double price, Date date, String paymentMethod) {
        this.supplier = supplier;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
