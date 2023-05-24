package com.example.demo.service.inventory;

import com.example.demo.model.Inventory;
import com.example.demo.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public void saveInventory(Inventory inventory) {
        this.inventoryRepository.save(inventory);
    }

    @Override
    public Inventory getInventoryById(long id) {
        Optional<Inventory> optional = inventoryRepository.findById(id);
        Inventory inventory = null;
        if(optional.isPresent()){
            inventory=optional.get();
        }else {
            throw new RuntimeException("Inventory not found for id :: "+id);
        }
        return inventory;
    }

    @Override
    public void deleteAllInventory() {
        this.inventoryRepository.deleteAll();
    }

    @Override
    public void deleteInventoryById(long id) {
        this.inventoryRepository.deleteById(id);
    }

    public List<Inventory> search(String keyword) {
        return this.inventoryRepository.search(keyword);
    }
}
