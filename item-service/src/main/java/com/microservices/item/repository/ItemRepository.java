package com.microservices.item.repository;

import com.microservices.item.entity.Item;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByItemNameStartingWith(String word);
    Page<Item> findByItemPriceBetween(Pageable page, BigDecimal minimum, BigDecimal maximum);
}
