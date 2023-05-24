package com.example.demo.repository;

import com.example.demo.model.Category;
import com.example.demo.model.Inventory;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query(value = "SELECT c FROM Category c WHERE c.name LIKE CONCAT('%',:keyword,'%')")
    List<Category> search(@Param("keyword") String keyword);
}
