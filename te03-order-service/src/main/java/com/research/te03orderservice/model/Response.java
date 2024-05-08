package com.research.te03orderservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
	String orderResponse;
	String inventoryResponse;
	String paymentResponse;
	String finalStatus;
}
