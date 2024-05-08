package com.research.te01orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.grpc.InventoryMessage;
import com.research.grpc.PaymentMessage;
import com.research.te01orderservice.constants.RequestCodes;
import com.research.te01orderservice.constants.ResponseCodes;
import com.research.te01orderservice.data.service.DataService;
import com.research.te01orderservice.grpc.service.InventoryService;
import com.research.te01orderservice.grpc.service.PaymentService;
import com.research.te01orderservice.model.HttpRequestData;
import com.research.te01orderservice.model.OrderRequest;
import com.research.te01orderservice.model.Response; 
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DataService dataService;

	public Response createOrder(HttpRequestData httpRequestData) throws JsonProcessingException {
		OrderRequest orderRequest = objectMapper.readValue(httpRequestData.getRequestBody(), OrderRequest.class);
		Response orderResponse = new Response();
		InventoryMessage inventoryResponse = inventoryService.callInventoryService(orderRequest.getInventoryResponse());
		PaymentMessage paymentResponse = paymentService.callPaymentService(orderRequest.getPaymentResponse());orderResponse.setOrderResponse(determineOrderStatus(orderRequest.getOrderResponse()));
		orderResponse.setReferenceId(httpRequestData.getReference());
		orderResponse.setInventoryResponse(inventoryResponse.getInventoryStatus());
		orderResponse.setPaymentResponse(paymentResponse.getPaymentStatus());
		orderResponse.setFinalStatus(determineFinalStatus(orderResponse));
		orderResponse.setOrderToInventorySendTime(inventoryResponse.getOrderToInventorySendTime());
		orderResponse.setOrderToInventoryReceivedTime(inventoryResponse.getOrderToInventoryReceivedTime());
		orderResponse.setInventoryToOrderSendTime(inventoryResponse.getInventoryToOrderSendTime());
		orderResponse.setInventoryToOrderReceivedTime(inventoryResponse.getInventoryToOrderReceivedTime());
		orderResponse.setOrderToPaymentSendTime(paymentResponse.getOrderToPaymentSendTime());
		orderResponse.setOrderToPaymentReceivedTime(paymentResponse.getOrderToPaymentReceivedTime());
		orderResponse.setPaymentToOrderSendTime(paymentResponse.getPaymentToOrderSendTime());
		orderResponse.setPaymentToOrderReceivedTime(paymentResponse.getPaymentToOrderReceivedTime());
		dataService.saveOrder(orderResponse);
		return orderResponse;
	}

	private String determineOrderStatus(String orderStatus) {
		if (RequestCodes.AVAILABLE.name().equals(orderStatus)) {
			return ResponseCodes.SUCCESS.name();
		} else if (RequestCodes.UNAVAILABLE.name().equals(orderStatus)) {
			return ResponseCodes.FAILURE.name();
		} else {
			return ResponseCodes.ERROR.name();
		}
	}

	private String determineFinalStatus(Response orderResponse) {
		if (orderResponse.getOrderResponse().equals(ResponseCodes.SUCCESS.name()) &&
				orderResponse.getInventoryResponse().equals(ResponseCodes.SUCCESS.name()) &&
				orderResponse.getPaymentResponse().equals(ResponseCodes.SUCCESS.name())) {
			return ResponseCodes.SUCCESS.name();
		} else if (orderResponse.getOrderResponse().equals(ResponseCodes.ERROR.name()) ||
				orderResponse.getInventoryResponse().equals(ResponseCodes.ERROR.name()) ||
				orderResponse.getPaymentResponse().equals(ResponseCodes.ERROR.name())) {
			return ResponseCodes.ERROR.name();
		} else {
			return ResponseCodes.FAILURE.name();
		}
	}
}
