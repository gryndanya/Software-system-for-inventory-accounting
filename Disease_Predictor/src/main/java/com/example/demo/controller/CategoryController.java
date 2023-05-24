package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.category.CategoryService;
import com.example.demo.service.inventory.InventoryService;
import com.example.demo.service.items_event.ItemsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemsEventService itemsEventService;

    @Autowired
    private InventoryService inventoryService;

    private String nameUpdate;

    @GetMapping("/category")
    public String showInventory(Model model){
        List<User> listUsers = repo.findAll();
        model.addAttribute("listUsers",listUsers);
        model.addAttribute("listCategory",categoryService.getAllCategory());
        return "inventory_category/category";
    }
    @GetMapping("/showNewCategoryForm")
    public String showNewInventoryForm(Model model){
        Category category = new Category();
        model.addAttribute("category",category);
        return "inventory_category/new_category";
    }

    @PostMapping("/saveCategory")
    public String saveInventory(@ModelAttribute("category")Category category,Model model){
        boolean flag = true;
        for(Category c : categoryService.getAllCategory()){
            if (c.getName().toLowerCase().equals(category.getName().toLowerCase())) {
                flag = false;
                break;
            }
        }
        if(flag){
            categoryService.saveCategory(category);
            return "redirect:/category";
        }else{
            return "inventory_category/bad_category";
        }
    }

    @PostMapping("/updateCategory")
    public String saveInventory(@RequestParam(name="id") long id,@ModelAttribute("category")Category category,Model model){
        boolean flag = true;
        for(Category c : categoryService.getAllCategory()){
            if (c.getName().toLowerCase().equals(category.getName().toLowerCase()) &&
                    !c.getId().equals(id)) {
                flag = false;
                break;
            }
        }
        if(flag){
            for(Inventory inventory : inventoryService.getAllInventory()){
                if(inventory.getCategory().toLowerCase().equals(nameUpdate.toLowerCase())){
                    inventory.setCategory(category.getName());
                }
            }
            categoryService.saveCategory(category);
            return "redirect:/category";
        }else{
            model.addAttribute("id",id);
            return "inventory_category/bad_category_update";
        }
    }

    @GetMapping("/searchCategoryForm")
    public String searchForm(Model model){
        return "inventory_category/search_category";
    }

    @RequestMapping(value = "categories", method = RequestMethod.GET)
    public String showCategoryByName(@RequestParam (value = "search", required = false) String name, Model model) {
        model.addAttribute("name",name);
        model.addAttribute("search", categoryService.search(name));
        return "inventory_category/search_category";
    }

    @GetMapping("/showCategoryFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id")long id,Model model){
        Category category = categoryService.getCategoryById(id);
        nameUpdate = category.getName();
        model.addAttribute("category",category);
        return "inventory_category/update_category";
    }

    @GetMapping("/deleteAllCategory")
    public String deleteAllCategory(Model model){
        this.categoryService.deleteAllCategory();
        itemsEventService.deleteAllItemsEvent();
        inventoryService.deleteAllInventory();
        return "redirect:/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable(value = "id")long id,Model model){
        for(Inventory inventory : inventoryService.getAllInventory()){
            if(inventory.getCategory().equals(categoryService.getCategoryById(id).getName())){
                inventoryService.deleteInventoryById(inventory.getId());
            }
        }
        for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
            for (Inventory inventory : inventoryService.getAllInventory()){
                if(inventory.getCategory().equals(categoryService.getCategoryById(id).getName()) &&
                itemsEvent.getName().equals(inventory.getName())){
                    itemsEventService.deleteItemsEventById(itemsEvent.getId());
                    break;
                }
            }
        }
        this.categoryService.deleteCategoryById(id);
        return "redirect:/category";
    }
}
