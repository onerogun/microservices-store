package com.microservices.order.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {

	private Long itemId;
	private BigDecimal itemPrice;
	private String itemName;
	private String itemCategory;
	private boolean itemFeatured;
	private int itemLeftInStock;
	

}
