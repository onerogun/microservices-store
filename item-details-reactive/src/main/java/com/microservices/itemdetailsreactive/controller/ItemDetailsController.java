package com.microservices.itemdetailsreactive.controller;


import com.microservices.itemdetailsreactive.entity.ItemDetails;
import com.microservices.itemdetailsreactive.service.ItemDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/itemDetails")
@Slf4j
public class ItemDetailsController {

    private final ItemDetailsService itemDetailsService;

    public ItemDetailsController(ItemDetailsService itemDetailsService) {
        this.itemDetailsService = itemDetailsService;
    }

    @PostMapping(value = "/saveItemDetails")
    public void saveItemDetails(@RequestBody ItemDetails itemDetails) {
        log.info("Inside of saveItemDetails method of ItemDetailsController, item-details-reactive");
        itemDetailsService.saveItemDetails(itemDetails);
    }

    @GetMapping(value = "/getItemDetails/{itemId}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ItemDetails> getItemDetails(@PathVariable Long itemId) {
        log.info("Inside of getItemDetails method of ItemDetailsController, item-details-reactive");
        return itemDetailsService.getItemDetails(itemId);
    }

    @GetMapping(value = "/getItemDetailsfake", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ItemDetails> getItemDetailsfake() {
        log.info("Inside of getItemDetails method of ItemDetailsController, item-details-reactive");

        Map<String, String> map = new HashMap<>();
        map.put("value", "valuevalue");
        map.put("value1", "valuevalue1");
        map.put("value2", "valuevalue2");
        map.put("value3", "valuevalue3");

        ItemDetails itemDetails = new ItemDetails(2l, map);
        itemDetails.setId("dwer");

        return Mono.just(itemDetails);
    }

}
