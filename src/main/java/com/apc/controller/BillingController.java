package com.apc.controller;

import com.apc.billing.service.BillingService;
import com.apc.billing.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping("/invoices")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try {
            List<Invoice> invoices = billingService.listInvoices();
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        try {
            Invoice invoice = billingService.getInvoiceById(id);
            if (invoice != null) {
                return ResponseEntity.ok(invoice);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/invoices")
    public ResponseEntity<String> createInvoice(@RequestBody InvoiceRequest request) {
        try {
            if (request.getItems() != null && !request.getItems().isEmpty()) {
                // New integrated billing with inventory management
                billingService.createInvoiceWithItems(request.getItems(), request);
                return ResponseEntity.ok("Invoice created successfully with inventory updated");
            } else {
                // Create invoice with customer details
                billingService.createInvoiceWithDetails(request);
                return ResponseEntity.ok("Invoice created successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating invoice: " + e.getMessage());
        }
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<String> updateInvoice(@PathVariable Long id, @RequestBody InvoiceRequest request) {
        try {
            billingService.updateInvoiceWithDetails(id, request);
            return ResponseEntity.ok("Invoice updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating invoice: " + e.getMessage());
        }
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Long id) {
        try {
            billingService.removeInvoice(id);
            return ResponseEntity.ok("Invoice deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting invoice: " + e.getMessage());
        }
    }

    // DTO for request body
    public static class InvoiceRequest {
        private double totalAmount;
        private java.util.Map<String, Integer> items; // itemName -> quantity
        private String customerName;
        private String customerEmail;
        private String date;
        private String dueDate;
        private String status;
        private String notes;

        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
        
        public java.util.Map<String, Integer> getItems() { return items; }
        public void setItems(java.util.Map<String, Integer> items) { this.items = items; }
        
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        
        public String getCustomerEmail() { return customerEmail; }
        public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
        
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        
        public String getDueDate() { return dueDate; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}