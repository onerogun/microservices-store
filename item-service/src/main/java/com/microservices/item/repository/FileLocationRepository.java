package com.microservices.item.repository;

import com.microservices.item.VO.ItemPathList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileLocationRepository extends MongoRepository<ItemPathList, Long> {
}
