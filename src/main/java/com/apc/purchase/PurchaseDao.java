package com.apc.purchase;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PurchaseDao {
    
    @PersistenceContext
    private EntityManager entityManager;

    public void savePurchase(Purchase purchase) {
        entityManager.persist(purchase);
    }

    public List<Purchase> getAllPurchases() {
        return entityManager.createQuery("SELECT p FROM Purchase p", Purchase.class).getResultList();
    }
}