package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.ItemsEvent;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.event.EventService;
import com.example.demo.service.items_event.ItemsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private EventService eventService;

    @Autowired
    private ItemsEventService itemsEventService;

    private String nameUpdate;

    @GetMapping("/event")
    public String showEvent(Model model){
        List<User> listUsers = repo.findAll();
        model.addAttribute("listUsers",listUsers);
        model.addAttribute("listEvent",eventService.getAllEvent());
        return "event/event";
    }
    @GetMapping("/showNewEventForm")
    public String showNewEventForm(Model model){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String time = formatter.format(date);
        model.addAttribute("time",time);
        Event event = new Event();
        model.addAttribute("event",event);
        return "event/new_event";
    }

    @PostMapping("/saveEvent")
    public String saveEvent(@ModelAttribute("event")Event event,Model model){
        boolean flag = true;
        for(Event e : eventService.getAllEvent()){
            if (e.getName().toLowerCase().equals(event.getName().toLowerCase()) &&
                    e.getDate().equals(event.getDate())) {
                flag = false;
                break;
            }
        }
        if(flag){
            eventService.saveEvent(event);
            return "redirect:/event";
        }else{
            return "event/bad_event";
        }
    }

    @PostMapping("/updateEvent")
    public String updateEvent(@RequestParam(name="id") long id,@ModelAttribute("event")Event event,Model model){
        boolean flag = true;
        for(Event e : eventService.getAllEvent()){
            if (e.getName().toLowerCase().equals(event.getName().toLowerCase()) &&
                    e.getDate().equals(event.getDate()) &&
                    !e.getId().equals(id)){
                flag = false;
                break;
            }
        }
        if(flag){
            for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
                if(itemsEvent.getName_event().toLowerCase().equals(nameUpdate.toLowerCase())){
                    itemsEvent.setName_event(event.getName());
                }
            }
            eventService.saveEvent(event);
            return "redirect:/event";
        }else{
            model.addAttribute("id",id);
            return "event/bad_event_update";
        }
    }

    @GetMapping("/searchEventForm")
    public String searchEvent(Model model){
        return "event/search_event";
    }

    @RequestMapping(value = "events", method = RequestMethod.POST)
    public String showEventByName(@RequestParam (value = "search", required = false) String name, Model model) {
        model.addAttribute("name",name);
        model.addAttribute("search", eventService.search(name));
        return "event/search_event";
    }

    @GetMapping("/showEventFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id")long id,Model model){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String time = formatter.format(date);
        model.addAttribute("time",time);
        Event event = eventService.getEventById(id);
        nameUpdate = event.getName();
        model.addAttribute("event",event);
        return "event/update_event";
    }

    @GetMapping("/deleteAllEvent")
    public String deleteAllEvent(Model model){
        itemsEventService.deleteAllItemsEvent();
        this.eventService.deleteAllEvent();
        return "redirect:/event";
    }

    @GetMapping("/deleteEvent/{id}")
    public String deleteEvent(@PathVariable(value = "id")long id,Model model){
        for(ItemsEvent itemsEvent : itemsEventService.getAllItemsEvent()){
            if(itemsEvent.getName().equals(eventService.getEventById(id).getName())){
                itemsEventService.deleteItemsEventById(id);
            }
        }
        this.eventService.deleteEventById(id);
        return "redirect:/event";
    }
}
