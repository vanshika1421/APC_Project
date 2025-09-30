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

    @PostMapping("/invoices")
    public ResponseEntity<String> createInvoice(@RequestBody InvoiceRequest request) {
        try {
            billingService.createInvoice(request.getTotalAmount());
            return ResponseEntity.ok("Invoice created successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating invoice: " + e.getMessage());
        }
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<String> updateInvoice(@PathVariable int id, @RequestBody InvoiceRequest request) {
        try {
            billingService.updateInvoice(id, request.getTotalAmount());
            return ResponseEntity.ok("Invoice updated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating invoice: " + e.getMessage());
        }
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable int id) {
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

        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    }
}