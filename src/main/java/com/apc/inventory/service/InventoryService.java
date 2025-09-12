package com.apc.inventory.service;

import com.apc.inventory.dao.ItemDao;
import com.apc.inventory.model.Item;
import org.hibernate.Session;
import java.util.List;

public class InventoryService {
    private final ItemDao itemDao = new ItemDao();

    public void addItem(Session session, String name, int quantity, double price) {
        Item item = new Item(name, quantity, price);
        itemDao.saveItem(session, item);
    }

    public List<Item> listItems(Session session) {
        return itemDao.getAllItems(session);
    }

    public void updateItem(Session session, int id, int quantity, double price) {
        Item item = itemDao.getItemById(session, id);
        if (item != null) {
            item.setQuantity(quantity);
            item.setPrice(price);
            itemDao.updateItem(session, item);
        }
    }

    public void removeItem(Session session, int id) {
        Item item = itemDao.getItemById(session, id);
        if (item != null) {
            itemDao.deleteItem(session, item);
        }
    }
}
