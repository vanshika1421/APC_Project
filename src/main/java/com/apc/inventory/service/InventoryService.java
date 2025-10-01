package com.apc.inventory.service;

import com.apc.inventory.dao.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apc.inventory.model.Item;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private ItemRepository itemRepository;

    public void addItem(String name, int quantity, double price) {
        Item item = new Item(name, quantity, price);
        itemRepository.save(item);
    }

    public List<Item> listItems() {
        return itemRepository.findAll();
    }

    public void updateItem(String id, int quantity, double price) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            item.setQuantity(quantity);
            item.setPrice(price);
            itemRepository.save(item);
        }
    }

    public void removeItem(String id) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isPresent()) {
            itemRepository.delete(itemOpt.get());
        } else {
            throw new RuntimeException("Item with ID " + id + " not found");
        }
    }

    // ===== INTEGRATED STOCK MANAGEMENT METHODS =====

    /**
     * Reduce stock quantity when items are sold (used by billing)
     */
    public boolean reduceStock(String itemName, int quantity) {
        Optional<Item> itemOpt = itemRepository.findByName(itemName);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            if (item.getQuantity() >= quantity) {
                item.setQuantity(item.getQuantity() - quantity);
                itemRepository.save(item);
                return true;
            }
        }
        return false; // Insufficient stock or item not found
    }

    /**
     * Increase stock quantity when items are purchased (used by purchase)
     */
    public void increaseStock(String itemName, int quantity, double price) {
        Optional<Item> itemOpt = itemRepository.findByName(itemName);
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            // Item exists, increase quantity and update price if different
            item.setQuantity(item.getQuantity() + quantity);
            item.setPrice(price); // Update to latest purchase price
            itemRepository.save(item);
        } else {
            // Item doesn't exist, create new item
            addItem(itemName, quantity, price);
        }
    }

    /**
     * Check if sufficient stock is available
     */
    public boolean isStockAvailable(String itemName, int quantity) {
        Optional<Item> itemOpt = itemRepository.findByName(itemName);
        return itemOpt.isPresent() && itemOpt.get().getQuantity() >= quantity;
    }

    /**
     * Get item by name
     */
    public Item getItemByName(String name) {
        return itemRepository.findByName(name).orElse(null);
    }
}