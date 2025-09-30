package com.apc.billing.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double totalAmount;
    private Date date;

    public Invoice() {}

    public Invoice(double totalAmount, Date date) {
        this.totalAmount = totalAmount;
        this.date = date;
    }

 
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
