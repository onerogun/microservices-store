package com.microservices.itemdetailsreactive.controller;


import com.microservices.itemdetailsreactive.entity.ItemDetails;
import com.microservices.itemdetailsreactive.entity.ItemDetailsList;
import com.microservices.itemdetailsreactive.entity.ItemRating;
import com.microservices.itemdetailsreactive.entity.ItemReview;
import com.microservices.itemdetailsreactive.service.ItemDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

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
    public Flux<ItemDetailsList> getItemDetailsfake() {
        log.info("Inside of getItemDetails method of ItemDetailsController, item-details-reactive");

        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setNameOf("keyy");
        itemDetails.setValueOf("valuee");


        Flux<ItemDetailsList> detailsListFlux = Flux.fromStream(
                        Stream.of( new ItemDetailsList("asdasdasd",73l, List.of(itemDetails)),
                                new ItemDetailsList("asdasdasd",74l, List.of(itemDetails)),
        new ItemDetailsList("asdasdasd",75l, List.of(itemDetails)),
        new ItemDetailsList("asdasdasd",76l, List.of(itemDetails))));

        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(5));

        return Flux.zip(detailsListFlux, durationFlux).map(Tuple2::getT1);
    }

    @GetMapping(value = "/getItemsReviews/{itemId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ItemReview> getItemReviews(@PathVariable Long itemId) {
        log.info("Inside of getItemReviews method of ItemDetailsController, item-details-reactive");

      return itemDetailsService.getItemReviews(itemId);
    }

    @PostMapping(value = "/saveItemReview")
    public void saveItemReview(@RequestBody ItemReview itemReview) {
        log.info("Inside of saveItemReview method of ItemDetailsController, item-details-reactive");
        log.info(itemReview.toString());
        itemDetailsService.saveItemReview(itemReview);
    }

    @GetMapping(value = "/getItemRating/{itemId}")
    public Mono<ItemRating> getItemRating(@PathVariable Long itemId) {
        log.info("Inside of getItemRating method of ItemDetailsController, item-details-reactive");
        return itemDetailsService.getItemRating(itemId);
    }

}
