package com.microservices.itemdetailsreactive.service;

import com.microservices.itemdetailsreactive.entity.ItemDetailsList;
import com.microservices.itemdetailsreactive.entity.ItemRating;
import com.microservices.itemdetailsreactive.entity.ItemReview;
import com.microservices.itemdetailsreactive.repository.ItemDetailsRepository;
import com.microservices.itemdetailsreactive.repository.ItemRatingRepository;
import com.microservices.itemdetailsreactive.repository.ItemReviewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ItemDetailsService {


    private final ItemDetailsRepository itemDetailsRepository;
    private final ItemReviewsRepository itemReviewsRepository;
    private final ItemRatingRepository itemRatingRepository;

    public ItemDetailsService(ItemDetailsRepository itemDetailsRepository, ItemReviewsRepository itemReviewsRepository, ItemRatingRepository itemRatingRepository) {
        this.itemDetailsRepository = itemDetailsRepository;
        this.itemReviewsRepository = itemReviewsRepository;
        this.itemRatingRepository = itemRatingRepository;
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

    public Flux<ItemReview> getItemReviews(Long itemId) {
        log.info("Inside of getItemReviews method of ItemDetailsService, item-details-reactive");
        return itemReviewsRepository.findByItemId(itemId);
    }

    public void saveItemReview(ItemReview itemReview) {
        log.info("Inside of saveItemReview method of ItemDetailsService, item-details-reactive");
        itemReviewsRepository.existsByReviewOwner(itemReview.getReviewOwner()).subscribe(aBoolean ->
                itemReviewsRepository.deleteByReviewOwner(itemReview.getReviewOwner()).subscribe(System.out::println)
                );
        itemReviewsRepository.save(itemReview).subscribe(System.out::println);
        calculateAndSaveItemRating(itemReview.getItemId(), itemReview.getRating());
    }

    private void calculateAndSaveItemRating(Long itemId,Integer rating) {
        itemRatingRepository.existsById(itemId).subscribe(aBoolean -> {
            ItemRating itemRating;
            if(aBoolean){
                itemRating = itemRatingRepository.findById(itemId).block();
                Double total = Double.valueOf(itemRating.getItemRating() * itemRating.getNumberOfRatings());
                Float newRating = (float) ((total + rating)/ (itemRating.getNumberOfRatings()+1));
                itemRating.setItemRating(newRating);
                itemRating.setNumberOfRatings(itemRating.getNumberOfRatings()+1);
            } else {
                itemRating = new ItemRating();
                itemRating.setItemId(itemId);
                itemRating.setItemRating(Float.valueOf(rating));
                itemRating.setNumberOfRatings(1L);
            }
            itemRatingRepository.save(itemRating).subscribe(System.out::println);
        });
    }

    public Mono<ItemRating> getItemRating(Long itemId) {
        return itemRatingRepository.findById(itemId);
    }
}
