package com.microservices.itemdetailsreactive.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@ToString
@Document(collection = "Itemdetails")
public class ItemDetails {
    @Id
    private String id;
    private Long itemId;
    private Map<String, String> details;

    public ItemDetails(Long itemId,Map<String, String> details) {
        this.itemId = itemId;
        this.details = details;
    }
}
