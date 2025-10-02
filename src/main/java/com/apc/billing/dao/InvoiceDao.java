package com.apc.billing.dao;

import com.apc.billing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Long> {
    // JpaRepository already provides save(), findById(), findAll(), delete(), etc.
}
