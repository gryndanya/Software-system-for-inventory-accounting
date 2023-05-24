package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.ItemsEvent;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.Inventory;
import com.example.demo.service.category.CategoryService;
import com.example.demo.service.inventory.InventoryService;
import com.example.demo.service.items_event.ItemsEventService;
import com.example.demo.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class InventoryController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private StockService stockService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemsEventService itemsEventService;

    private int quantityUpdate;
    private int quantityStockUpdate;
    private String nameUpdate;

    @GetMapping("/inventory")
    public String showInventory(Model model){

        List<User> listUsers = repo.findAll();
        model.addAttribute("listUsers",listUsers);

        model.addAttribute("listInventory",inventoryService.getAllInventory());
        return "inventory/inventory";
    }
    @GetMapping("/showNewInventoryForm")
    public String showNewInventoryForm(Model model){
        Inventory inventory = new Inventory();
        model.addAttribute("listCategory",categoryService.getAllCategory());
        model.addAttribute("listStock",stockService.getAllStock());
        model.addAttribute("inventory",inventory);
        return "inventory/new_inventory";
    }

    @PostMapping("/saveInventory")
    public String saveInventory(@ModelAttribute("inventory")Inventory inventory,Model model){
        boolean flag = true;
        for(Inventory i : inventoryService.getAllInventory()){
            if (i.getName().toLowerCase().equals(inventory.getName().toLowerCase()) &&
                    i.getAddress_of_stock().equals(inventory.getAddress_of_stock()) &&
                    i.getQuantity() == inventory.getQuantity()){
                flag = false;
                break;
            }
        }
        if(flag){
            inventory.setQuantity_in_stock(inventory.getQuantity());
            inventoryService.saveInventory(inventory);
            return "redirect:/inventory";
        }else{
            return "inventory/bad_inventory";
        }
    }

    @PostMapping("/updateInventory")
    public String saveInventory(@RequestParam(name="id") long id,@ModelAttribute("inventory")Inventory inventory,Model model){
        boolean flag = true;
        for(Inventory i : inventoryService.getAllInventory()){
            if ((i.getName().toLowerCase().equals(inventory.getName().toLowerCase()) &&
                    i.getAddress_of_stock().equals(inventory.getAddress_of_stock()) &&
                    i.getQuantity() == inventory.getQuantity() &&
                    i.getId()!=id)||
                    (quantityUpdate-quantityStockUpdate>inventory.getQuantity())){
                flag = false;
                break;
            }
        }
        if(flag){
            if(inventory.getQuantity()<=quantityUpdate){
                inventory.setQuantity_in_stock(quantityStockUpdate-(quantityUpdate-inventory.getQuantity()));
            }else if(inventory.getQuantity()>quantityUpdate){
                inventory.setQuantity_in_stock(quantityStockUpdate+(inventory.getQuantity()-quantityUpdate));
            }
            for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
                if(itemsEvent.getName().equals(inventoryService.getInventoryById(id).getName())){
                    itemsEventService.getItemsEventById(itemsEvent.getId()).setName(inventory.getName());
                }
            }
            inventoryService.saveInventory(inventory);
            return "redirect:/inventory";
        }else{
            model.addAttribute("id",id);
            return "inventory/bad_inventory_update";
        }
    }

    @GetMapping("/searchInventoryForm")
    public String searchForm(Model model){
        return "inventory/search_form";
    }

    @RequestMapping(value = "inventories", method = RequestMethod.GET)
    public String showInventoryByName(@RequestParam (value = "search", required = false) String name, Model model) {
        model.addAttribute("name",name);
        model.addAttribute("search", inventoryService.search(name));
        return "inventory/search_form";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id")long id,Model model){
        Inventory inventory = inventoryService.getInventoryById(id);
        model.addAttribute("listCategory",categoryService.getAllCategory());
        model.addAttribute("listStock",stockService.getAllStock());
        model.addAttribute("inventory",inventory);
        quantityUpdate = inventory.getQuantity();
        quantityStockUpdate = inventory.getQuantity_in_stock();
        nameUpdate = inventory.getName();
        return "inventory/update_inventory";
    }

    @GetMapping("/deleteAllInventory")
    public String deleteAllInventory(Model model){
        itemsEventService.getAllItemsEvent();
        this.inventoryService.deleteAllInventory();
        return "redirect:/inventory";
    }

    @GetMapping("/deleteInventory/{id}")
    public String deleteInventory(@PathVariable(value = "id")long id,Model model){
        for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
            if(itemsEvent.getName().equals(inventoryService.getInventoryById(id).getName())){
                itemsEventService.deleteItemsEventById(itemsEvent.getId());
            }
        }
        this.inventoryService.deleteInventoryById(id);
        return "redirect:/inventory";
    }
}
