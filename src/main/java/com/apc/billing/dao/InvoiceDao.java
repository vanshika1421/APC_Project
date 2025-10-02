package com.apc.billing.dao;

import com.apc.billing.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceDao extends MongoRepository<Invoice, String> {
    // MongoRepository already provides save(), findById(), findAll(), delete(), etc.
}
