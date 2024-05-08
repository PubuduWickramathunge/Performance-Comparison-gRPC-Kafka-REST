package com.research.te04paymentservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
	private String referenceId;
	private String orderResponse;
	private String inventoryResponse;
	private String paymentResponse;
	private String finalStatus;
	private String errorMessage;
	private String orderToInventorySendTime;
	private String orderToInventoryReceivedTime;
	private String orderToPaymentSendTime;
	private String orderToPaymentReceivedTime;
	private String inventoryToOrderSendTime;
	private String inventoryToOrderReceivedTime;
	private String paymentToOrderSendTime;
	private String paymentToOrderReceivedTime;
}
