package com.apc.inventory.dao;

import com.apc.inventory.model.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class ItemDao {
    public void saveItem(Session session, Item item) {
        Transaction tx = session.beginTransaction();
        session.save(item);
        tx.commit();
    }

    public Item getItemById(Session session, int id) {
        return session.get(Item.class, id);
    }

    public List<Item> getAllItems(Session session) {
        Query<Item> query = session.createQuery("from Item", Item.class);
        return query.list();
    }

    public void updateItem(Session session, Item item) {
        Transaction tx = session.beginTransaction();
        session.update(item);
        tx.commit();
    }

    public void deleteItem(Session session, Item item) {
        Transaction tx = session.beginTransaction();
        session.delete(item);
        tx.commit();
    }
}
