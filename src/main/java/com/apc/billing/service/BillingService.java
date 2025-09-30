package com.apc.billing.service;

import com.apc.billing.dao.InvoiceDao;
import com.apc.billing.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class BillingService {
    @Autowired
    private InvoiceDao invoiceDao;

    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
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