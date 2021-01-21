package com.microservices.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name="orders")	//Add table name to avoid wrong SQL hibernate join relation
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long orderId;

	private LocalDateTime orderTime;
	private Long customerId;
	private BigDecimal orderTotal;


	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	@EqualsAndHashCode.Exclude		//to avoid LOMBOK errors
	private List<OrderItem> orderItems;


}
