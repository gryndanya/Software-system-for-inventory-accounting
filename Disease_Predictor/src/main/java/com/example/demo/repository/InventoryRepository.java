package com.example.demo.repository;

import com.example.demo.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query(value = "SELECT c FROM Inventory c WHERE c.name LIKE CONCAT('%',:keyword,'%')")
    List<Inventory> search(@Param("keyword") String keyword);
}
