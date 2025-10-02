package com.apc.billing.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalAmount;
    private Date date;
    private Date dueDate;
    private String customerName;
    private String customerEmail;
    private String status = "pending";
    private String notes;

    public Invoice() {}

    public Invoice(double totalAmount, Date date) {
        this.totalAmount = totalAmount;
        this.date = date;
        this.status = "pending";
    }

    public Invoice(double totalAmount, Date date, String customerName, String customerEmail, Date dueDate, String status, String notes) {
        this.totalAmount = totalAmount;
        this.date = date;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.dueDate = dueDate;
        this.status = status != null ? status : "pending";
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
