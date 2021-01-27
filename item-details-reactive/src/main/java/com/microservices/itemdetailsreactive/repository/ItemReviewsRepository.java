package com.microservices.itemdetailsreactive.repository;

import com.microservices.itemdetailsreactive.entity.ItemReview;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemReviewsRepository extends ReactiveMongoRepository<ItemReview, String> {
    Flux<ItemReview> findByItemId(Long itemId);
    Mono<Boolean> existsByReviewOwner(Long reviewOwner);
    Mono<ItemReview> deleteByReviewOwner(Long reviewOwner);
}
