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
@Document(collection = "Itemreviews")
public class ItemReview {

    @Id
    private String id;
    private Long itemId;
    private Long reviewOwner;
    private String review;
    private Integer rating;
}
