package com.microservices.order.converter;

import com.microservices.order.VO.Item;
import com.microservices.order.entity.OrderItem;
import com.microservices.order.wrapper.OrderContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConverterOrderContentOrderItem implements Converter<OrderContent, OrderItem> {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public OrderItem convert(OrderContent source) {
        Item item = restTemplate.getForObject("http://item-service/items/getItem/" + source.getItemId(), Item.class );

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemItemId(item.getItemId());
        orderItem.setOrderItemCategory(item.getItemCategory());
        orderItem.setOrderItemName(item.getItemName());
        orderItem.setOrderItemPrice(item.getItemPrice());
        orderItem.setOrderItemFeatured(item.isItemFeatured());
        orderItem.setOrderItemAmountOrdered(source.getItemAmount());
        orderItem.setOrderItemOwner(item.getItemOwner());

        return orderItem;
    }

}
