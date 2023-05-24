package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.Inventory;
import com.example.demo.model.ItemsEvent;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.event.EventService;
import com.example.demo.service.inventory.InventoryService;
import com.example.demo.service.items_event.ItemsEventService;
import com.example.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ItemsEventController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ItemsEventService itemsEventService;

    @Autowired
    private InventoryService inventoryService;

    private String nameEvent="";
    private long id=0;
    private int quantityUpdate;
    private String nameEventUpdate;
    @GetMapping("/listItems")
    public String showListItems(Model model){
        List<User> listUsers = new ArrayList<>();
        for(User user : userService.getAllUser()){
            if(!user.getFirstName().equals("admin")){
                listUsers.add(user);
            }
        }
        model.addAttribute("listUsers",listUsers);
        model.addAttribute("listItemsEvent",itemsEventService.getAllItemsEvent());
        return "list_of_items/list";
    }

    @GetMapping("/listItems/{id}")
    public String showListOfItems(@PathVariable(value = "id")long id,Model model){
        List<ItemsEvent> itemsEvents = new ArrayList<>();
        for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
            if(itemsEvent.getName_event().equals(eventService.getEventById(id).getName())){
                itemsEvents.add(itemsEvent);
            }
        }
        model.addAttribute("listItemsEvent",itemsEvents);
        model.addAttribute("event",eventService.getEventById(id));

        this.id = id;
        nameEvent = eventService.getEventById(id).getName();

        return "list_of_items/list_of_items";
    }

    @GetMapping("/addItem")
    public String addItem(Model model){
        ItemsEvent itemsEvent = new ItemsEvent();
        model.addAttribute("id",id);
        model.addAttribute("items_event",itemsEvent);
        List<User> listUsers = new ArrayList<>();
        for(User user : userService.getAllUser()){
            if(!user.getFirstName().equals("admin")){
                listUsers.add(user);
            }
        }
        model.addAttribute("listPerson", listUsers);

        List<Inventory> inventoryList = new ArrayList<>();
        for (Inventory inventory : inventoryService.getAllInventory()){
            if(inventory.getQuantity_in_stock()>0){
                inventoryList.add(inventory);
            }
        }
        model.addAttribute("listItems",inventoryList);
        return "list_of_items/add_item";
    }

    @PostMapping("/saveItem")
    public String saveEvent(@ModelAttribute("items_event")ItemsEvent itemsEvent,Model model){
        boolean flag = true;
        Inventory selectedInventory = null;
        for(Inventory inventory : inventoryService.getAllInventory()){
            if(inventory.getName().equals(itemsEvent.getName())){
                selectedInventory = inventory;
            }
        }
        if(selectedInventory.getQuantity_in_stock()<itemsEvent.getQuantity_on_event()) {
            flag = false;
        }
        for(ItemsEvent e : itemsEventService.getAllItemsEvent()){
            if ((e.getName().toLowerCase().equals(itemsEvent.getName().toLowerCase()) &&
                    e.getPerson().equals(itemsEvent.getPerson()) &&
                    e.getName_event().equals(nameEvent)) ||
                    selectedInventory.getQuantity_in_stock()<itemsEvent.getQuantity_on_event()) {
                flag = false;
                break;
            }
        }
        if(flag){
            itemsEvent.setName_event(nameEvent);
            selectedInventory.setQuantity_in_stock(selectedInventory.getQuantity_in_stock()-itemsEvent.getQuantity_on_event());
            inventoryService.saveInventory(selectedInventory);
            itemsEventService.saveItemsEvent(itemsEvent);
            return "redirect:/listItems/"+this.id;
        }else{
            model.addAttribute("id",this.id);
            model.addAttribute("items_event",itemsEvent);
            List<User> listUsers = new ArrayList<>();
            for(User user : userService.getAllUser()){
                if(user.getFirstName()!="admin"){
                    listUsers.add(user);
                }
            }
            model.addAttribute("listPerson", listUsers);
            model.addAttribute("listItems",inventoryService.getAllInventory());
            model.addAttribute("inventory",selectedInventory);
            return "list_of_items/add_item_bad";
        }
    }

    @PostMapping("/updateItem")
    public String saveEvent(@RequestParam(name="id") long id,@ModelAttribute("items_event")ItemsEvent itemsEvent,Model model){
        boolean flag = true;
        Inventory selectedInventory = null;
        for(Inventory inventory : inventoryService.getAllInventory()){
            if(inventory.getName().equals(itemsEvent.getName())){
                selectedInventory = inventory;
            }
        }
        for(ItemsEvent e : itemsEventService.getAllItemsEvent()){
            if ((e.getName().toLowerCase().equals(itemsEvent.getName().toLowerCase()) &&
                    e.getPerson().equals(itemsEvent.getPerson()) &&
                    e.getId()!=id &&
                    e.getName_event().equals(nameEvent) && quantityUpdate==itemsEvent.getQuantity_on_event()) ||
                    ((selectedInventory.getQuantity_in_stock()+quantityUpdate)<itemsEvent.getQuantity_on_event())) {
                flag = false;
                break;
            }
        }
        if(flag){
            itemsEvent.setName_event(nameEventUpdate);
            if(itemsEvent.getQuantity_on_event()<=quantityUpdate){
                selectedInventory.setQuantity_in_stock(quantityUpdate-itemsEvent.getQuantity_on_event()+selectedInventory.getQuantity_in_stock());
            }else if(itemsEvent.getQuantity_on_event()>quantityUpdate){
                selectedInventory.setQuantity_in_stock(selectedInventory.getQuantity_in_stock()+quantityUpdate-itemsEvent.getQuantity_on_event());
            }
            inventoryService.saveInventory(selectedInventory);
            itemsEventService.saveItemsEvent(itemsEvent);
            return "redirect:/listItems/"+this.id;
        }else{
            model.addAttribute("id",this.id);
            model.addAttribute("id_back",id);
            model.addAttribute("items_event",itemsEvent);
            List<User> listUsers = new ArrayList<>();
            for(User user : userService.getAllUser()){
                if(user.getFirstName()!="admin"){
                    listUsers.add(user);
                }
            }
            model.addAttribute("listPerson",listUsers);
            model.addAttribute("listItems",inventoryService.getAllInventory());
            model.addAttribute("inventory",selectedInventory);
            return "list_of_items/update_item_bad";
        }
    }

    @PostMapping("/updateAllItem")
    public String saveAllEvent(@RequestParam(name="id") long id,@ModelAttribute("items_event")ItemsEvent itemsEvent,Model model){
        boolean flag = true;
        Inventory selectedInventory = null;
        for(Inventory inventory : inventoryService.getAllInventory()){
            if(inventory.getName().equals(itemsEvent.getName())){
                selectedInventory = inventory;
            }
        }
        for(ItemsEvent e : itemsEventService.getAllItemsEvent()){
            if ((e.getName().toLowerCase().equals(itemsEvent.getName().toLowerCase()) &&
                    e.getPerson().equals(itemsEvent.getPerson()) &&
                    e.getId()!=id &&
                    e.getName_event().equals(nameEvent) && quantityUpdate==itemsEvent.getQuantity_on_event()) ||
                    ((selectedInventory.getQuantity_in_stock()+quantityUpdate)<itemsEvent.getQuantity_on_event())) {
                flag = false;
                break;
            }
        }
        if(flag){
            itemsEvent.setName_event(nameEventUpdate);
            if(itemsEvent.getQuantity_on_event()<=quantityUpdate){
                selectedInventory.setQuantity_in_stock(quantityUpdate-itemsEvent.getQuantity_on_event()+selectedInventory.getQuantity_in_stock());
            }else if(itemsEvent.getQuantity_on_event()>quantityUpdate){
                selectedInventory.setQuantity_in_stock(selectedInventory.getQuantity_in_stock()+quantityUpdate-itemsEvent.getQuantity_on_event());
            }
            inventoryService.saveInventory(selectedInventory);
            itemsEventService.saveItemsEvent(itemsEvent);
            return "redirect:/listItems";
        }else{
            model.addAttribute("id",this.id);
            model.addAttribute("id_back",id);
            model.addAttribute("items_event",itemsEvent);
            List<User> listUsers = new ArrayList<>();
            for(User user : userService.getAllUser()){
                if(user.getFirstName()!="admin"){
                    listUsers.add(user);
                }
            }
            model.addAttribute("listPerson", listUsers);
            model.addAttribute("listItems",inventoryService.getAllInventory());
            model.addAttribute("inventory",selectedInventory);
            return "list_of_items/update_item_bad";
        }
    }

    @GetMapping("/searchItemsEventForm")
    public String searchEvent(Model model){
        model.addAttribute("id",this.id);
        return "list_of_items/search_item";
    }

    @GetMapping("/searchItemsEventPersonForm")
    public String searchPersonEvent(Model model){
        model.addAttribute("id",this.id);
        return "list_of_items/search_item_person";
    }

    @RequestMapping(value = "items_event", method = RequestMethod.GET)
    public String showItemsByName(@RequestParam (value = "search", required = false) String name, Model model) {
        model.addAttribute("name",name);
        List<ItemsEvent> itemsEvents = new ArrayList<>();
        for(ItemsEvent itemsEvent : itemsEventService.search(name)){
            if(itemsEvent.getName_event().equals(nameEvent)){
                itemsEvents.add(itemsEvent);
            }
        }
        model.addAttribute("search", itemsEvents);
        model.addAttribute("id",this.id);
        return "list_of_items/search_item";
    }

    @RequestMapping(value = "items_events", method = RequestMethod.GET)
    public String showPeopleByName(@RequestParam (value = "search", required = false) String name, Model model) {
        model.addAttribute("name",name);
        List<ItemsEvent> itemsEvents = new ArrayList<>();
        for(ItemsEvent itemsEvent : itemsEventService.searchPerson(name)){
            if(itemsEvent.getName_event().equals(nameEvent)){
                itemsEvents.add(itemsEvent);
            }
        }
        model.addAttribute("search", itemsEvents);
        model.addAttribute("id",this.id);
        return "list_of_items/search_item_person";
    }


    @GetMapping("/showItemsEventFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id")long id,Model model){

        model.addAttribute("id",this.id);
        List<User> listUsers = new ArrayList<>();
        for(User user : userService.getAllUser()){
            if(user.getFirstName()!="admin"){
                listUsers.add(user);
            }
        }
        model.addAttribute("listPerson",listUsers);
        model.addAttribute("listItems",inventoryService.getAllInventory());

        ItemsEvent itemsEvent = itemsEventService.getItemsEventById(id);
        quantityUpdate = itemsEvent.getQuantity_on_event();
        nameEventUpdate = itemsEvent.getName_event();
        model.addAttribute("items_event",itemsEvent);
        return "list_of_items/update_item";
    }

    @GetMapping("/showAllItemsEventFormForUpdate/{id}")
    public String showAllFormForUpdate(@PathVariable(value = "id")long id,Model model){

        model.addAttribute("id",this.id);
        List<User> listUsers = new ArrayList<>();
        for(User user : userService.getAllUser()){
            if(user.getFirstName()!="admin"){
                listUsers.add(user);
            }
        }
        model.addAttribute("listPerson",listUsers);
        model.addAttribute("listItems",inventoryService.getAllInventory());

        ItemsEvent itemsEvent = itemsEventService.getItemsEventById(id);
        quantityUpdate = itemsEvent.getQuantity_on_event();
        nameEventUpdate = itemsEvent.getName_event();
        model.addAttribute("items_event",itemsEvent);
        return "list_of_items/update_all_item";
    }

    @GetMapping("/deleteAllItemsEvent")
    public String deleteAllItems(Model model){
        Inventory selectedInventory = null;
        for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
            for (Inventory inventory : inventoryService.getAllInventory()){
                if(inventory.getName().equals(itemsEvent.getName()) &&
                    itemsEvent.getName_event().equals(nameEvent)){
                    selectedInventory = inventory;
                    selectedInventory.setQuantity_in_stock(selectedInventory.getQuantity_in_stock()+itemsEvent.getQuantity_on_event());
                    inventoryService.saveInventory(selectedInventory);
                    itemsEventService.deleteItemsEventById(itemsEvent.getId());
                    break;
                }
            }
        }
        return "redirect:/listItems/"+this.id;
    }

    @GetMapping("/deleteItemsEvent/{id}")
    public String deleteItem(@PathVariable(value = "id")long id,Model model){
        Inventory selectedInventory = null;
        for(Inventory inventory : inventoryService.getAllInventory()){
            if(inventory.getName().equals(itemsEventService.getItemsEventById(id).getName())){
                selectedInventory = inventory;
            }
        }
        selectedInventory.setQuantity_in_stock(selectedInventory.getQuantity_in_stock()+itemsEventService.getItemsEventById(id).getQuantity_on_event());
        inventoryService.saveInventory(selectedInventory);
        this.itemsEventService.deleteItemsEventById(id);
        return "redirect:/listItems/"+this.id;
    }

    @GetMapping("/deleteItemsEventAll/{id}")
    public String deleteItemAll(@PathVariable(value = "id")long id,Model model){
        Inventory selectedInventory = null;
        for(Inventory inventory : inventoryService.getAllInventory()){
            if(inventory.getName().equals(itemsEventService.getItemsEventById(id).getName())){
                selectedInventory = inventory;
            }
        }
        selectedInventory.setQuantity_in_stock(selectedInventory.getQuantity_in_stock()+itemsEventService.getItemsEventById(id).getQuantity_on_event());
        inventoryService.saveInventory(selectedInventory);
        this.itemsEventService.deleteItemsEventById(id);
        return "redirect:/listItems";
    }
}
