package com.microservices.item.service;

import com.microservices.item.VO.OrderContentList;
import com.microservices.item.entity.Item;
import com.microservices.item.repository.ItemRepository;
import com.microservices.item.wrapper.ItemList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;


@Service
@Slf4j
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;


    public ItemList getItems() {
        log.info("Inside of getItems method  of ItemService class, item-service");
        boolean i = true;
        return new ItemList(itemRepository.findAll());
    }

    public Item addItem(Item item) {
        log.info("Inside of getItems method  of ItemService class, item-service");
        return itemRepository.save(item);
    }

    public Item getItem(Long itemId) {
        log.info("Inside of getItem method  of ItemService class, item-service");
        return itemRepository.findById(itemId).get();
    }

    public boolean updateStock(OrderContentList orderContentList) {
        AtomicBoolean updateSuccessful = new AtomicBoolean(true);
        orderContentList.getOrderContentList().forEach( orderContent -> {
            Item item = itemRepository.findById(orderContent.getItemId()).get();
            if(orderContent.getItemAmount() > item.getItemLeftInStock() ) {
                updateSuccessful.set(false);
            }
            item.setItemLeftInStock(item.getItemLeftInStock() - orderContent.getItemAmount());
            itemRepository.save(item);

        });

        return updateSuccessful.get();
    }
}
