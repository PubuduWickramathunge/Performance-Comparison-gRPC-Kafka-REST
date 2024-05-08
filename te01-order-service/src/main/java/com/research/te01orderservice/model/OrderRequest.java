package com.research.te01orderservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
	private String orderResponse;
	private String inventoryResponse;
	private String paymentResponse;

}
