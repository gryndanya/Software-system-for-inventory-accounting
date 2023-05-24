package com.example.demo.service.category;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void saveCategory(Category category) {
        this.categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(long id) {
        Optional<Category> optional = categoryRepository.findById(id);
        Category category = null;
        if(optional.isPresent()){
            category=optional.get();
        }else {
            throw new RuntimeException("Inventory not found for id :: "+id);
        }
        return category;
    }

    @Override
    public void deleteAllCategory() {
        this.categoryRepository.deleteAll();
    }

    @Override
    public void deleteCategoryById(long id) {
        this.categoryRepository.deleteById(id);
    }

    public List<Category> search(String keyword) {
        return this.categoryRepository.search(keyword);
    }
}
