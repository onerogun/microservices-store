package com.microservices.order.repository;

import com.microservices.order.entity.SavedCart;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface SavedCartRepository extends MongoRepository<SavedCart,Long> {
}
