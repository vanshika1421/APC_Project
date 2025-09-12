package com.apc.billing.service;

import com.apc.billing.dao.InvoiceDao;
import com.apc.billing.model.Invoice;
import org.hibernate.Session;
import java.util.Date;
import java.util.List;

public class BillingService {
    private final InvoiceDao invoiceDao = new InvoiceDao();

    public void createInvoice(Session session, double totalAmount) {
        Invoice invoice = new Invoice(totalAmount, new Date());
        invoiceDao.saveInvoice(session, invoice);
    }

    public List<Invoice> listInvoices(Session session) {
        return invoiceDao.getAllInvoices(session);
    }

    public void updateInvoice(Session session, int id, double totalAmount) {
        Invoice invoice = invoiceDao.getInvoiceById(session, id);
        if (invoice != null) {
            invoice.setTotalAmount(totalAmount);
            invoiceDao.updateInvoice(session, invoice);
        }
    }

    public void removeInvoice(Session session, int id) {
        Invoice invoice = invoiceDao.getInvoiceById(session, id);
        if (invoice != null) {
            invoiceDao.deleteInvoice(session, invoice);
        }
    }
}
