package com.microservices.order.converter;

import com.microservices.order.entity.OrderItem;
import com.microservices.order.wrapper.OrderContent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemOrderContentConverter implements Converter<OrderItem, OrderContent> {

    @Override
    public OrderContent convert(OrderItem orderItem) {
        OrderContent orderContent = new OrderContent();
        orderContent.setItemAmount(orderItem.getOrderItemAmountOrdered());
        orderContent.setItemId(orderItem.getOrderItemItemId());
        return orderContent;
    }
}
