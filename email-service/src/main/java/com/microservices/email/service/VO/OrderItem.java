package com.microservices.email.service.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {

	private Long orderItemId;
	private int orderItemAmountOrdered;
	
	private Long orderItemItemId;
	private BigDecimal orderItemPrice;
	private String orderItemName;
	private String orderItemCategory;
	private boolean orderItemFeatured;

	private Long orderId;

}
