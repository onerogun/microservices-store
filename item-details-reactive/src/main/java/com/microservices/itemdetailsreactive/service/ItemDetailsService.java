package com.microservices.itemdetailsreactive.service;

import com.microservices.itemdetailsreactive.entity.ItemDetails;
import com.microservices.itemdetailsreactive.entity.ItemDetailsList;
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


    public Mono<ItemDetailsList> getItemDetailsList(Long itemId) {
        log.info("Inside of getItemDetails method of ItemDetailsService, item-details-reactive");
        return itemDetailsRepository.findByItemId(itemId);
    }

    public void saveItemDetails(ItemDetailsList itemDetailsList) {
        log.info("Inside of saveItemDetails method of ItemDetailsService, item-details-reactive");
        itemDetailsRepository.existsByItemId(itemDetailsList.getItemId()).subscribe(aBoolean -> {
            if (aBoolean) {
                itemDetailsRepository.deleteByItemId(itemDetailsList.getItemId()).subscribe(System.out::println);
            }
        } );
        itemDetailsRepository.save(itemDetailsList).subscribe(itemDetailsList1 -> System.out.println("Saved: " + itemDetailsList1));
    }
}
