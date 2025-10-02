package com.apc.billing.service;

import com.apc.billing.dao.InvoiceDao;
import com.apc.billing.model.Invoice;
import com.apc.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BillingService {
    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private InventoryService inventoryService;

    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    /**
     * Create invoice with integrated inventory management
     * @param items Map of itemName -> quantity to be sold
     * @return true if invoice created successfully, false if insufficient stock
     */
    @Transactional
    public boolean createInvoiceWithItems(Map<String, Integer> items) {
        return createInvoiceWithItems(items, null);
    }
    
    /**
     * Create invoice with integrated inventory management and customer details
     * @param items Map of itemName -> quantity to be sold
     * @param customerDetails Invoice request with customer information
     * @return true if invoice created successfully, false if insufficient stock
     */
    @Transactional
    public boolean createInvoiceWithItems(Map<String, Integer> items, Object customerDetails) {
        // First, check if all items have sufficient stock
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            
            if (!inventoryService.isStockAvailable(itemName, quantity)) {
                throw new RuntimeException("Insufficient stock for item: " + itemName);
            }
        }
        
        // Calculate total amount and reduce stock
        double totalAmount = 0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            
            // Get item price and calculate amount
            var item = inventoryService.getItemByName(itemName);
            if (item != null) {
                totalAmount += item.getPrice() * quantity;
                // Reduce stock quantity
                inventoryService.reduceStock(itemName, quantity);
            }
        }
        
        // Create invoice with customer details if provided
        Invoice invoice;
        if (customerDetails != null) {
            invoice = createInvoiceWithCustomerDetails(totalAmount, customerDetails);
        } else {
            invoice = new Invoice(totalAmount, new Date());
        }
        invoiceDao.save(invoice);
        return true;
    }

    @Transactional
    public void createInvoice(double totalAmount) {
        Invoice invoice = new Invoice(totalAmount, new Date());
        invoiceDao.save(invoice);
    }

    @Transactional(readOnly = true)
    public List<Invoice> listInvoices() {
        return invoiceDao.findAll();
    }

    @Transactional(readOnly = true)
    public Invoice getInvoiceById(String id) {
        return invoiceDao.findById(id).orElse(null);
    }

    @Transactional
    public void updateInvoice(String id, double totalAmount) {
        Invoice invoice = invoiceDao.findById(id).orElse(null);
        if (invoice != null) {
            invoice.setTotalAmount(totalAmount);
            invoiceDao.save(invoice);
        }
    }

    @Transactional
    public void removeInvoice(String id) {
        Invoice invoice = invoiceDao.findById(id).orElse(null);
        if (invoice != null) {
            invoiceDao.delete(invoice);
        }
    }
    
    @Transactional
    public void createInvoiceWithDetails(Object requestDetails) {
        Invoice invoice = createInvoiceWithCustomerDetails(0.0, requestDetails);
        invoiceDao.save(invoice);
    }
    
    @Transactional
    public void updateInvoiceWithDetails(String id, Object requestDetails) {
        Invoice invoice = invoiceDao.findById(id).orElse(null);
        if (invoice != null) {
            updateInvoiceFromRequest(invoice, requestDetails);
            invoiceDao.save(invoice);
        }
    }
    
    private Invoice createInvoiceWithCustomerDetails(double totalAmount, Object requestDetails) {
        try {
            // Use reflection to get values from request object
            Class<?> clazz = requestDetails.getClass();
            
            String customerName = getStringField(clazz, requestDetails, "customerName");
            String customerEmail = getStringField(clazz, requestDetails, "customerEmail");
            String dateStr = getStringField(clazz, requestDetails, "date");
            String dueDateStr = getStringField(clazz, requestDetails, "dueDate");
            String status = getStringField(clazz, requestDetails, "status");
            String notes = getStringField(clazz, requestDetails, "notes");
            double amount = getDoubleField(clazz, requestDetails, "totalAmount");
            
            if (totalAmount == 0.0) totalAmount = amount;
            
            Date date = parseDate(dateStr);
            Date dueDate = parseDate(dueDateStr);
            
            return new Invoice(totalAmount, date, customerName, customerEmail, dueDate, status, notes);
        } catch (Exception e) {
            // Fallback to basic invoice
            return new Invoice(totalAmount, new Date());
        }
    }
    
    private void updateInvoiceFromRequest(Invoice invoice, Object requestDetails) {
        try {
            Class<?> clazz = requestDetails.getClass();
            
            String customerName = getStringField(clazz, requestDetails, "customerName");
            String customerEmail = getStringField(clazz, requestDetails, "customerEmail");
            String dateStr = getStringField(clazz, requestDetails, "date");
            String dueDateStr = getStringField(clazz, requestDetails, "dueDate");
            String status = getStringField(clazz, requestDetails, "status");
            String notes = getStringField(clazz, requestDetails, "notes");
            double amount = getDoubleField(clazz, requestDetails, "totalAmount");
            
            System.out.println("DEBUG: Updating invoice with amount: " + amount);
            System.out.println("DEBUG: Customer name: " + customerName);
            
            invoice.setCustomerName(customerName);
            invoice.setCustomerEmail(customerEmail);
            invoice.setTotalAmount(amount);
            invoice.setStatus(status);
            invoice.setNotes(notes);
            
            if (dateStr != null) {
                Date date = parseDate(dateStr);
                if (date != null) invoice.setDate(date);
            }
            
            if (dueDateStr != null) {
                Date dueDate = parseDate(dueDateStr);
                if (dueDate != null) invoice.setDueDate(dueDate);
            }
        } catch (Exception e) {
            // If reflection fails, just update the amount
            System.err.println("Error updating invoice details: " + e.getMessage());
        }
    }
    
    private String getStringField(Class<?> clazz, Object obj, String fieldName) {
        try {
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            var method = clazz.getMethod(methodName);
            Object result = method.invoke(obj);
            return result != null ? result.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private double getDoubleField(Class<?> clazz, Object obj, String fieldName) {
        try {
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            var method = clazz.getMethod(methodName);
            Object result = method.invoke(obj);
            System.out.println("DEBUG: getDoubleField - fieldName: " + fieldName + ", result: " + result + ", type: " + (result != null ? result.getClass().getSimpleName() : "null"));
            return result != null ? Double.parseDouble(result.toString()) : 0.0;
        } catch (Exception e) {
            System.out.println("DEBUG: getDoubleField exception for " + fieldName + ": " + e.getMessage());
            return 0.0;
        }
    }
    
    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            // Handle ISO date format from frontend
            if (dateStr.contains("T")) {
                return new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dateStr);
            } else {
                return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            }
        } catch (Exception e) {
            return new Date(); // Default to current date
        }
    }
}