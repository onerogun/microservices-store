package com.microservices.itemdetailsreactive.controller;


import com.microservices.itemdetailsreactive.entity.ItemDetails;
import com.microservices.itemdetailsreactive.entity.ItemDetailsList;
import com.microservices.itemdetailsreactive.service.ItemDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/itemDetails")
@Slf4j
public class ItemDetailsController {

    private final ItemDetailsService itemDetailsService;

    public ItemDetailsController(ItemDetailsService itemDetailsService) {
        this.itemDetailsService = itemDetailsService;
    }

    @PostMapping(value = "/saveItemDetails")
    public void saveItemDetails(@RequestBody ItemDetailsList itemDetailsList) {
        log.info("Inside of saveItemDetails method of ItemDetailsController, item-details-reactive");
        itemDetailsService.saveItemDetails(itemDetailsList);
    }

    @GetMapping(value = "/getItemDetails/{itemId}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ItemDetailsList> getItemDetails(@PathVariable Long itemId) {
        log.info("Inside of getItemDetails method of ItemDetailsController, item-details-reactive");
        return itemDetailsService.getItemDetailsList(itemId);
    }

    @GetMapping(value = "/getItemDetailsfake", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ItemDetailsList> getItemDetailsfake() {
        log.info("Inside of getItemDetails method of ItemDetailsController, item-details-reactive");

        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setNameOf("keyy");
        itemDetails.setValueOf("valuee");



        return Mono.just(new ItemDetailsList("asdasdasd",73l, List.of(itemDetails)));
    }

}
