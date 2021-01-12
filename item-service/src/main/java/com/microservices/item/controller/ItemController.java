package com.microservices.item.controller;

import com.microservices.item.VO.OrderContentList;
import com.microservices.item.VO.PathObj;
import com.microservices.item.VO.PathObjList;
import com.microservices.item.entity.Item;
import com.microservices.item.service.ItemService;
import com.microservices.item.wrapper.ItemList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;


    @GetMapping("/getItems")
    public ResponseEntity<ItemList> getItems() throws InterruptedException {
        log.info("Inside of getItems method of ItemController Class in item-service");
        return new ResponseEntity<>(itemService.getItems(), HttpStatus.OK);
    }

    @GetMapping("/getItemsPage")
    public ResponseEntity<Map<String, Object>> getItemsPage(@RequestParam(name = "pageNo", defaultValue = "0") String pageNo,
                                                            @RequestParam(name = "pageSize", defaultValue = "5") String pageSize,
                                                            @RequestParam(name = "sortBy", defaultValue = "itemPrice") String sortBy
                                                            ) {
        log.info("Inside of getItemsPage method of ItemController Class in item-service");
        Map<String, Object> response =   itemService.getItemsByPage(Integer.valueOf(pageNo), Integer.valueOf(pageSize), sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getItem/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable Long itemId) {
        log.info("Inside of getItem method of ItemController Class in item-service");
        return  new ResponseEntity<>(itemService.getItem(itemId), HttpStatus.OK);
    }

    @PostMapping("/addItem")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        log.info("Inside of addItem method of ItemController Class in item-service");
        log.info(item.toString());
        if (item != null) {
            return new ResponseEntity<>(itemService.addItem(item), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/updateItem")
    public ResponseEntity<Item> updateItem(@RequestBody Item item) {
        log.info("Inside of updateItem method of ItemController Class in item-service");
        Item updatedItem = itemService.updateItem(item);
        return new ResponseEntity(updatedItem, updatedItem == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    @DeleteMapping("/deleteItem/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        log.info("Inside of deleteItem method of ItemController Class in item-service");
        if(itemService.deleteItem(itemId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
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

    @GetMapping("/getItemFileLocations/{itemId}")
    public ResponseEntity<PathObjList> getItemFileLocations(@PathVariable Long itemId) {
       return new ResponseEntity<>(itemService.getItemFileLocations(itemId), HttpStatus.OK);
    }
}
