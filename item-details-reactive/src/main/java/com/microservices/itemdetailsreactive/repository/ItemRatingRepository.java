package com.microservices.itemdetailsreactive.repository;

import com.microservices.itemdetailsreactive.entity.ItemRating;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemRatingRepository extends ReactiveMongoRepository<ItemRating, Long> {
}
