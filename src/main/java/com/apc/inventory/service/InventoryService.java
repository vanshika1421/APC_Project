package com.apc.inventory.service;

import com.apc.inventory.dao.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apc.inventory.model.Item;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private ItemDao itemDao;

    public void setItemDao(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Transactional
    public void addItem(String name, int quantity, double price) {
        Item item = new Item(name, quantity, price);
        itemDao.saveItem(item);
    }

    @Transactional(readOnly = true)
    public List<Item> listItems() {
        return itemDao.getAllItems();
    }

    @Transactional
    public void updateItem(int id, int quantity, double price) {
        Item item = itemDao.getItemById(id);
        if (item != null) {
            item.setQuantity(quantity);
            item.setPrice(price);
            itemDao.updateItem(item);
        }
    }

    @Transactional
    public void removeItem(int id) {
        Item item = itemDao.getItemById(id);
        if (item != null) {
            itemDao.deleteItem(item);
        }
    }
}