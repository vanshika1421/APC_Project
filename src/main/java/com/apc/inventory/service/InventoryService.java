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
        } else {
            throw new RuntimeException("Item with ID " + id + " not found");
        }
    }

    // ===== INTEGRATED STOCK MANAGEMENT METHODS =====
    
    /**
     * Reduce stock quantity when items are sold (used by billing)
     */
    @Transactional
    public boolean reduceStock(String itemName, int quantity) {
        Item item = itemDao.getItemByName(itemName);
        if (item != null && item.getQuantity() >= quantity) {
            item.setQuantity(item.getQuantity() - quantity);
            itemDao.updateItem(item);
            return true;
        }
        return false; // Insufficient stock or item not found
    }

    /**
     * Increase stock quantity when items are purchased (used by purchase)
     */
    @Transactional
    public void increaseStock(String itemName, int quantity, double price) {
        Item item = itemDao.getItemByName(itemName);
        if (item != null) {
            // Item exists, increase quantity and update price if different
            item.setQuantity(item.getQuantity() + quantity);
            item.setPrice(price); // Update to latest purchase price
            itemDao.updateItem(item);
        } else {
            // Item doesn't exist, create new item
            addItem(itemName, quantity, price);
        }
    }

    /**
     * Check if sufficient stock is available
     */
    @Transactional(readOnly = true)
    public boolean isStockAvailable(String itemName, int quantity) {
        Item item = itemDao.getItemByName(itemName);
        return item != null && item.getQuantity() >= quantity;
    }

    /**
     * Get item by name
     */
    @Transactional(readOnly = true)
    public Item getItemByName(String name) {
        return itemDao.getItemByName(name);
    }
}