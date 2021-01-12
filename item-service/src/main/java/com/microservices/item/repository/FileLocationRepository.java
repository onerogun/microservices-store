package com.microservices.item.repository;

import com.microservices.item.VO.PathObj;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileLocationRepository extends MongoRepository<PathObj, String> {
    List<PathObj> findAllByItemId(Long itemId);
}
