package com.apc.billing.dao;

import com.apc.billing.model.Invoice;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class InvoiceDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    public void saveInvoice(Invoice invoice) {
        entityManager.persist(invoice);
    }

    public Invoice getInvoiceById(int id) {
        return entityManager.find(Invoice.class, id);
    }

    public List<Invoice> getAllInvoices() {
        return entityManager.createQuery("SELECT i FROM Invoice i", Invoice.class).getResultList();
    }

    public void updateInvoice(Invoice invoice) {
        entityManager.merge(invoice);
    }

    public void deleteInvoice(Invoice invoice) {
        entityManager.remove(entityManager.contains(invoice) ? invoice : entityManager.merge(invoice));
    }
}
