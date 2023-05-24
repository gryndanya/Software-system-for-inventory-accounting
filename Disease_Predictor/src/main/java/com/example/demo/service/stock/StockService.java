package com.example.demo.service.stock;

import com.example.demo.model.Category;
import com.example.demo.model.Event;
import com.example.demo.model.Inventory;
import com.example.demo.model.Stock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StockService {
    List<Stock> getAllStock();
    void saveStock(Stock stock);
    Stock getStockById(long id);
    void deleteAllStock();
    void deleteStockById(long id);

    List<Stock> search(String keyword);
}
