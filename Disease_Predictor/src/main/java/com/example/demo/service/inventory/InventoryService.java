package com.example.demo.service.inventory;

import com.example.demo.model.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryService {
    List<Inventory> getAllInventory();
    void saveInventory(Inventory inventory);
    Inventory getInventoryById(long id);
    void deleteAllInventory();
    void deleteInventoryById(long id);

    List<Inventory> search(String keyword);
}
