package com.example.demo.service.items_event;

import com.example.demo.model.Category;
import com.example.demo.model.ItemsEvent;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemsEventRepository;
import com.example.demo.service.items_event.ItemsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ItemsEventServiceImpl implements ItemsEventService {

    @Autowired
    private ItemsEventRepository itemsEventRepository;

    @Override
    public List<ItemsEvent> getAllItemsEvent() {
        return itemsEventRepository.findAll();
    }

    @Override
    public void saveItemsEvent(ItemsEvent itemsEvent) {
        this.itemsEventRepository.save(itemsEvent);
    }

    @Override
    public ItemsEvent getItemsEventById(long id) {
        Optional<ItemsEvent> optional = itemsEventRepository.findById(id);
        ItemsEvent itemsEvent = null;
        if(optional.isPresent()){
            itemsEvent=optional.get();
        }else {
            throw new RuntimeException("Inventory not found for id :: "+id);
        }
        return itemsEvent;
    }

    @Override
    public void deleteAllItemsEvent() {
        this.itemsEventRepository.deleteAll();
    }

    @Override
    public void deleteItemsEventById(long id) {
        this.itemsEventRepository.deleteById(id);
    }

    public List<ItemsEvent> search(String keyword) {
        return this.itemsEventRepository.search(keyword);
    }

    public List<ItemsEvent> searchPerson(String keyword) {
        return this.itemsEventRepository.searchPerson(keyword);
    }
}
