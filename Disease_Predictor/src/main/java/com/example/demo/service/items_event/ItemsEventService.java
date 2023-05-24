package com.example.demo.service.items_event;

import com.example.demo.model.Category;
import com.example.demo.model.Inventory;
import com.example.demo.model.ItemsEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemsEventService {
    List<ItemsEvent> getAllItemsEvent();
    void saveItemsEvent(ItemsEvent itemsEvent);
    ItemsEvent getItemsEventById(long id);
    void deleteAllItemsEvent();
    void deleteItemsEventById(long id);

    List<ItemsEvent> search(String keyword);
    List<ItemsEvent> searchPerson(String keyword);

}
