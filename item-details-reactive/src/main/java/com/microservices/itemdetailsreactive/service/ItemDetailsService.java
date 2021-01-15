package com.microservices.itemdetailsreactive.service;

import com.microservices.itemdetailsreactive.entity.ItemDetails;
import com.microservices.itemdetailsreactive.repository.ItemDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ItemDetailsService {


    private final ItemDetailsRepository itemDetailsRepository;

    public ItemDetailsService(ItemDetailsRepository itemDetailsRepository) {
        this.itemDetailsRepository = itemDetailsRepository;
    }


    public Mono<ItemDetails> getItemDetails(Long itemId) {
        log.info("Inside of getItemDetails method of ItemDetailsService, item-details-reactive");
        return itemDetailsRepository.findByItemId(itemId);
    }

    public void saveItemDetails(ItemDetails itemDetails) {
        log.info("Inside of saveItemDetails method of ItemDetailsService, item-details-reactive");
        itemDetailsRepository.existsByItemId(itemDetails.getItemId()).subscribe(aBoolean -> {
            if (aBoolean) {
                itemDetailsRepository.deleteByItemId(itemDetails.getItemId()).subscribe(System.out::println);
            }
        } );

        itemDetailsRepository.insert(itemDetails).subscribe(System.out::println);
    }
}
