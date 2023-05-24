package com.example.demo.repository;

import com.example.demo.model.Inventory;
import com.example.demo.model.ItemsEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsEventRepository extends JpaRepository<ItemsEvent, Long> {
    @Query(value = "SELECT c FROM ItemsEvent c WHERE c.name LIKE CONCAT('%',:keyword,'%')")
    List<ItemsEvent> search(@Param("keyword") String keyword);

    @Query(value = "SELECT c FROM ItemsEvent c WHERE c.person LIKE CONCAT('%',:keyword,'%')")
    List<ItemsEvent> searchPerson(@Param("keyword") String keyword);
}
