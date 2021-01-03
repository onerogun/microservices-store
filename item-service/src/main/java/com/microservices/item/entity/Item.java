package com.microservices.item.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicUpdate
public class Item {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long itemId;
	private BigDecimal itemPrice;
	private String itemName;
	private String itemCategory;
	private boolean itemFeatured;
	private int itemLeftInStock;
	

}
