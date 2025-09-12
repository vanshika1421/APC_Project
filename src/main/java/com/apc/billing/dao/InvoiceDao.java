package com.apc.billing.dao;

import com.apc.billing.model.Invoice;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class InvoiceDao {
    public void saveInvoice(Session session, Invoice invoice) {
        Transaction tx = session.beginTransaction();
        session.save(invoice);
        tx.commit();
    }

    public Invoice getInvoiceById(Session session, int id) {
        return session.get(Invoice.class, id);
    }

    public List<Invoice> getAllInvoices(Session session) {
        Query<Invoice> query = session.createQuery("from Invoice", Invoice.class);
        return query.list();
    }

    public void updateInvoice(Session session, Invoice invoice) {
        Transaction tx = session.beginTransaction();
        session.update(invoice);
        tx.commit();
    }

    public void deleteInvoice(Session session, Invoice invoice) {
        Transaction tx = session.beginTransaction();
        session.delete(invoice);
        tx.commit();
    }
}
