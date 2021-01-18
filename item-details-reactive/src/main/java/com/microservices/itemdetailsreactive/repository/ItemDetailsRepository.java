package com.microservices.itemdetailsreactive.repository;

import com.microservices.itemdetailsreactive.entity.ItemDetails;
import com.microservices.itemdetailsreactive.entity.ItemDetailsList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface ItemDetailsRepository extends ReactiveMongoRepository<ItemDetailsList, String> {
    Mono<ItemDetailsList> findByItemId(Long itemId);
    Mono<Boolean> existsByItemId(Long itemId);
    Flux<ItemDetailsList> deleteByItemId(Long itemId);
}
