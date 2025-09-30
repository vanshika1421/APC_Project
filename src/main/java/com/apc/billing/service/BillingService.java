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
        
        // Create invoice
        Invoice invoice = new Invoice(totalAmount, new Date());
        invoiceDao.saveInvoice(invoice);
        return true;
    }

    @Transactional
    public void createInvoice(double totalAmount) {
        Invoice invoice = new Invoice(totalAmount, new Date());
        invoiceDao.saveInvoice(invoice);
    }

    @Transactional(readOnly = true)
    public List<Invoice> listInvoices() {
        return invoiceDao.getAllInvoices();
    }

    @Transactional
    public void updateInvoice(int id, double totalAmount) {
        Invoice invoice = invoiceDao.getInvoiceById(id);
        if (invoice != null) {
            invoice.setTotalAmount(totalAmount);
            invoiceDao.updateInvoice(invoice);
        }
    }

    @Transactional
    public void removeInvoice(int id) {
        Invoice invoice = invoiceDao.getInvoiceById(id);
        if (invoice != null) {
            invoiceDao.deleteInvoice(invoice);
        }
    }
}