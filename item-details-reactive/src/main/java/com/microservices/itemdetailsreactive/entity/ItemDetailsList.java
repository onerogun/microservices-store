package com.microservices.itemdetailsreactive.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "Itemdetails")
public class ItemDetailsList {
    @Id
    private String id;
    private Long itemId;
    private List<ItemDetails> itemDetailsList;

    public ItemDetailsList(Long itemId, List<ItemDetails> itemDetailsList) {
        this.itemId = itemId;
        this.itemDetailsList = itemDetailsList;
    }
}
