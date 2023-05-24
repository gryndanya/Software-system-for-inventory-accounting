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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemsEventService itemsEventService;

    @Autowired
    private InventoryService inventoryService;

    private String emailUpdate;
    private String passwordUpdate;
    private String nameUpdate;
    private String surnameUpdate;

    @GetMapping("/list_users_edit")
    public String viewUsersEditList(Model model){
        List<User> listUsers = new ArrayList<>();
        for(User user : userService.getAllUser()){
            if(!user.getFirstName().equals("admin")){
                listUsers.add(user);
            }
        }
        model.addAttribute("listUsers",listUsers);

        return "user/users_edit";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model){
        List<User> listUsers = new ArrayList<>();
        for(User user : userService.getAllUser()){
            if(!user.getFirstName().equals("admin")){
                listUsers.add(user);
            }
        }
        model.addAttribute("listUsers",listUsers);

        return "user/users";
    }

    @GetMapping("/showNewUserForm")
    public String showNewUserForm(Model model){
        model.addAttribute("user",new User());
        return "user/new_user";
    }

    @PostMapping("/saveUser")
    public String saveUser(User user,Model model) throws Exception{
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if(repo.findByEmail(user.getEmail())!=null) {
            model.addAttribute("error","An account already exists.");
            return "user/register_bad_user";
        }else{
            user.setRole("USER");
            userService.saveUser(user);
            return "redirect:/list_users_edit";
        }
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam(name="id") long id,@ModelAttribute("user")User user,Model model){
        user.setPassword(passwordUpdate);
        if(repo.findByEmail(user.getEmail())!=null && !emailUpdate.equals(user.getEmail())) {
            model.addAttribute("error","An account already exists.");
            model.addAttribute("id",id);
            return "user/register_bad_update_user";
        }else{
            repo.update(id,user.getEmail(),user.getFirstName(),user.getLastName(),user.getPassword(),"USER");
            for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
                if(itemsEvent.getPerson().equals(nameUpdate+' '+ surnameUpdate+' '+ emailUpdate)){
                    ItemsEvent itemsEvent1 = itemsEvent;
                    itemsEvent1.setPerson(user.getFirstName()+' '+user.getLastName()+' '+user.getEmail());
                    itemsEventService.saveItemsEvent(itemsEvent1);
                }
            }
            return "redirect:/list_users_edit";
        }
    }

    @GetMapping("/showUserFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id")long id,Model model){
        User user = userService.getUserById(id);
        emailUpdate = user.getEmail();
        passwordUpdate = user.getPassword();
        nameUpdate = user.getFirstName();
        surnameUpdate = user.getLastName();
        model.addAttribute("user",user);

        return "user/user_update";
    }

    @GetMapping("/deleteAllUser")
    public String deleteAllEvent(Model model){
        for(User user : userService.getAllUser()){
            if(!user.getFirstName().equals("admin")){
                for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
                    if(itemsEvent.getPerson()
                            .equals(userService.getUserById(user.getId()).getFirstName()+' '+
                                    userService.getUserById(user.getId()).getLastName()+' '+
                                    userService.getUserById(user.getId()).getEmail())){
                        Inventory selectedInventory = null;
                        for(Inventory inventory : inventoryService.getAllInventory()){
                            if(inventory.getName().equals(itemsEventService.getItemsEventById(itemsEvent.getId()).getName())){
                                selectedInventory = inventory;
                            }
                        }
                        selectedInventory.setQuantity_in_stock(selectedInventory.getQuantity_in_stock()+itemsEventService.getItemsEventById(itemsEvent.getId()).getQuantity_on_event());
                        inventoryService.saveInventory(selectedInventory);
                        itemsEventService.deleteItemsEventById(itemsEvent.getId());
                    }
                }
                userService.deleteUserById(user.getId());
            }
        }
        return "redirect:/list_users_edit";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteEvent(@PathVariable(value = "id")long id,Model model){
        for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
            if(itemsEvent.getPerson()
                    .equals(userService.getUserById(id).getFirstName()+' '+
                            userService.getUserById(id).getLastName()+' '+
                            userService.getUserById(id).getEmail())){
                Inventory selectedInventory = null;
                for(Inventory inventory : inventoryService.getAllInventory()){
                    if(inventory.getName().equals(itemsEventService.getItemsEventById(itemsEvent.getId()).getName())){
                        selectedInventory = inventory;
                    }
                }
                selectedInventory.setQuantity_in_stock(selectedInventory.getQuantity_in_stock()+itemsEventService.getItemsEventById(itemsEvent.getId()).getQuantity_on_event());
                inventoryService.saveInventory(selectedInventory);
                itemsEventService.deleteItemsEventById(itemsEvent.getId());
            }
        }
        userService.deleteUserById(id);
        return "redirect:/list_users_edit";
    }
}
