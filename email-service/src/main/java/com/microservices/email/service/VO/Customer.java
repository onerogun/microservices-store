package com.microservices.email.service.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {

	private Long customerId;
	private String customerName;
	private String customerEMail;
	

}
