package com.apc.inventory.dao;

import com.apc.inventory.model.Item;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    public void saveItem(Item item) {
        entityManager.persist(item);
    }

    public Item getItemById(int id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> getAllItems() {
        return entityManager.createQuery("SELECT i FROM Item i", Item.class).getResultList();
    }

    public void updateItem(Item item) {
        entityManager.merge(item);
    }

    public void deleteItem(Item item) {
        entityManager.remove(entityManager.contains(item) ? item : entityManager.merge(item));
    }
}
