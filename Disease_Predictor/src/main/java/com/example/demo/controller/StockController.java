package com.example.demo.controller;

import com.example.demo.model.ItemsEvent;
import com.example.demo.model.Stock;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.Inventory;
import com.example.demo.service.inventory.InventoryService;
import com.example.demo.service.items_event.ItemsEventService;
import com.example.demo.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StockController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private StockService stockService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ItemsEventService itemsEventService;

    private String nameUpdate;
    @GetMapping("/stock")
    public String showStock(Model model){
        List<User> listUsers = repo.findAll();
        model.addAttribute("listUsers",listUsers);
        model.addAttribute("listStock",stockService.getAllStock());
        return "stock/stock";
    }
    @GetMapping("/showNewStockForm")
    public String showNewStockForm(Model model){
        Stock stock = new Stock();
        model.addAttribute("stock",stock);
        return "stock/new_stock";
    }

    @PostMapping("/saveStock")
    public String saveStock(@ModelAttribute("stock")Stock stock,Model model){
        boolean flag = true;
        for(Stock st : stockService.getAllStock()){
            if (st.getName().toLowerCase().equals(stock.getName().toLowerCase())) {
                flag = false;
                break;
            }
        }
        if(flag){
            stockService.saveStock(stock);
            return "redirect:/stock";
        }else{
            return "stock/bad_stock";
        }
    }

    @PostMapping("/updateStock")
    public String saveStock(@RequestParam(name="id") long id,@ModelAttribute("stock")Stock stock,Model model){
        boolean flag = true;
        for(Stock st : stockService.getAllStock()){
            if (st.getName().toLowerCase().equals(stock.getName().toLowerCase())
                    && !st.getId().equals(id)) {
                flag = false;
                break;
            }
        }
        if(flag){
            for(Inventory inventory : inventoryService.getAllInventory()){
                if(inventory.getAddress_of_stock().toLowerCase().equals(nameUpdate.toLowerCase())){
                    inventory.setAddress_of_stock(stock.getName());
                }
            }
            stockService.saveStock(stock);
            return "redirect:/stock";
        }else{
            model.addAttribute("id",id);
            return "stock/bad_stock_update";
        }
    }

    @GetMapping("/searchStockForm")
    public String searchForm(Model model){
        return "stock/search_stock";
    }

    @RequestMapping(value = "stocks", method = RequestMethod.GET)
    public String showStockByName(@RequestParam (value = "search", required = false) String name, Model model) {
        model.addAttribute("name",name);
        model.addAttribute("search", stockService.search(name));
        return "stock/search_stock";
    }

    @GetMapping("/showStockFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id")long id,Model model){
        Stock stock = stockService.getStockById(id);
        nameUpdate = stock.getName();
        model.addAttribute("stock",stock);
        return "stock/update_stock";
    }

    @GetMapping("/deleteAllStock")
    public String deleteAllStock(Model model){
        this.stockService.deleteAllStock();
        itemsEventService.deleteAllItemsEvent();
        inventoryService.deleteAllInventory();
        return "redirect:/stock";
    }

    @GetMapping("/deleteStock/{id}")
    public String deleteStock(@PathVariable(value = "id")long id,Model model){
        for(Inventory inventory : inventoryService.getAllInventory()){
            if(inventory.getAddress_of_stock().equals(stockService.getStockById(id).getName())){
                inventoryService.deleteInventoryById(inventory.getId());
            }
        }
        for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
            for (Inventory inventory : inventoryService.getAllInventory()){
                if(inventory.getAddress_of_stock().equals(stockService.getStockById(id).getName()) &&
                        itemsEvent.getName().equals(inventory.getName())){
                    itemsEventService.deleteItemsEventById(itemsEvent.getId());
                    break;
                }
            }
        }
        this.stockService.deleteStockById(id);
        return "redirect:/stock";
    }
}
