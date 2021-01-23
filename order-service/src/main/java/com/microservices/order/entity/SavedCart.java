package com.microservices.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Document(collection = "SavedCart")
public class SavedCart {

    @Id
    private Long customerId;

    private List<OrderItem> orderItemList;
}
