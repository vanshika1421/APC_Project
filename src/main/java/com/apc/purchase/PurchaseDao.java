package com.apc.purchase;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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