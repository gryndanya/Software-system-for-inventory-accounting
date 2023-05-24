package com.example.demo.service.category;

import com.example.demo.model.Category;
import com.example.demo.model.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getAllCategory();
    void saveCategory(Category category);
    Category getCategoryById(long id);
    void deleteAllCategory();
    void deleteCategoryById(long id);

    List<Category> search(String keyword);
}
