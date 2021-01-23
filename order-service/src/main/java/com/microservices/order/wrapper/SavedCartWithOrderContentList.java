package com.microservices.order.wrapper;

import com.microservices.order.entity.SavedCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedCartWithOrderContentList {
    private OrderContentList orderContentList;
    private SavedCart savedCart;
}
