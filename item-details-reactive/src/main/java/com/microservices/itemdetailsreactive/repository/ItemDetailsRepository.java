package com.microservices.itemdetailsreactive.repository;

import com.microservices.itemdetailsreactive.entity.ItemDetails;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface ItemDetailsRepository extends ReactiveMongoRepository<ItemDetails, String> {
    Mono<ItemDetails> findByItemId(Long itemId);
    Mono<Boolean> existsByItemId(Long itemId);
    Flux<ItemDetails> deleteByItemId(Long itemId);
}
