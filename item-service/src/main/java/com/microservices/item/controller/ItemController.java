package com.microservices.item.controller;

import com.microservices.item.VO.OrderContentList;
import com.microservices.item.entity.Item;
import com.microservices.item.service.ItemService;
import com.microservices.item.wrapper.ItemList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@CrossOrigin("*")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;


    @GetMapping("/getItems")
    public ItemList getItems() throws InterruptedException {
        log.info("Inside of getItems method of ItemController Class in item-service");
        return itemService.getItems();
    }

    @GetMapping("/getItem/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        log.info("Inside of getItem method of ItemController Class in item-service");
        return  itemService.getItem(itemId);
    }

    @PostMapping("/addItem")
    public Item addItem(@RequestBody Item item) {
        log.info("Inside of addItem method of ItemController Class in item-service");
        return itemService.addItem(item);
    }

    @PostMapping("/updateItem")
    public Item updateItem(@RequestBody Item item) {
        log.info("Inside of updateItem method of ItemController Class in item-service");
        return itemService.addItem(item);
    }

    @PostMapping("/updateStock")
    public ResponseEntity<String> updateStock(@RequestBody OrderContentList orderContentList) {
        boolean success = itemService.updateStock(orderContentList);
        if (success == true) {
            return ResponseEntity.ok("Update successful!");
        } else  {
            return ResponseEntity.badRequest().build();
        }
    }
}
