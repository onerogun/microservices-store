package com.microservices.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="orderitem")			//Add table name to avoid wrong SQL hibernate join relation
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long orderItemId;
	private int orderItemAmountOrdered;
	
	private Long orderItemItemId;
	private BigDecimal orderItemPrice;
	private String orderItemName;
	private String orderItemCategory;
	private boolean orderItemFeatured;

	@Column(name = "order_id")
	private Long orderId;

/* 	// Creates another table for relationship
	@ManyToOne
	@JoinColumn(name = "order_id")
	@EqualsAndHashCode.Exclude
	@JsonIgnore
	private  Order order;
*/

}
