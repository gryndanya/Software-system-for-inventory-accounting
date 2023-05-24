package com.example.demo.service.event;

import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService{

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> getAllEvent() {
        return eventRepository.findAll();
    }

    @Override
    public void saveEvent(Event event) {
        this.eventRepository.save(event);
    }

    @Override
    public Event getEventById(long id) {
        Optional<Event> optional = eventRepository.findById(id);
        Event event = null;
        if(optional.isPresent()){
            event=optional.get();
        }else {
            throw new RuntimeException("Inventory not found for id :: "+id);
        }
        return event;
    }


    @Override
    public void deleteAllEvent() {
        this.eventRepository.deleteAll();
    }

    @Override
    public void deleteEventById(long id) {
        this.eventRepository.deleteById(id);
    }

    public List<Event> search(String keyword) {
        return this.eventRepository.search(keyword);
    }
}
