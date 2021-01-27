package com.microservices.itemdetailsreactive.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Document(collection = "ItemRatings")
public class ItemRating{
    @Id
    private Long itemId;
    private Float itemRating;
    private Long numberOfRatings;
}
