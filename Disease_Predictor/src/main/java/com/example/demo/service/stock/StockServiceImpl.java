package com.example.demo.service.stock;

import com.example.demo.model.Category;
import com.example.demo.model.Stock;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public void saveStock(Stock stock) {
        this.stockRepository.save(stock);
    }

    @Override
    public Stock getStockById(long id) {
        Optional<Stock> optional = stockRepository.findById(id);
        Stock stock = null;
        if(optional.isPresent()){
            stock=optional.get();
        }else {
            throw new RuntimeException("Inventory not found for id :: "+id);
        }
        return stock;
    }

    @Override
    public void deleteAllStock() {
        this.stockRepository.deleteAll();
    }

    @Override
    public void deleteStockById(long id) {
        this.stockRepository.deleteById(id);
    }

    public List<Stock> search(String keyword) {
        return this.stockRepository.search(keyword);
    }
}
