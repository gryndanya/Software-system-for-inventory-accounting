package com.example.demo.service.event;

import com.example.demo.model.Category;
import com.example.demo.model.Event;
import com.example.demo.model.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    List<Event> getAllEvent();
    void saveEvent(Event event);
    Event getEventById(long id);
    void deleteAllEvent();
    void deleteEventById(long id);

    List<Event> search(String keyword);
}
