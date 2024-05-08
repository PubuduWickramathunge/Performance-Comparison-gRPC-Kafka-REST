package com.research.te04orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te04orderservice.constants.RequestCodes;
import com.research.te04orderservice.constants.ResponseCodes;
import com.research.te04orderservice.data.service.DataService;
import com.research.te04orderservice.model.HttpRequestData;
import com.research.te04orderservice.model.OrderRequest;
import com.research.te04orderservice.model.Response;
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

	public Response createOrder(HttpRequestData httpRequestData) throws Exception {
		OrderRequest orderRequest = objectMapper.readValue(httpRequestData.getRequestBody(), OrderRequest.class);
		Response orderResponse = new Response();

		try {
			orderResponse.setOrderResponse(determineOrderStatus(orderRequest.getOrderResponse()));
			if (orderResponse.getOrderResponse().equals(ResponseCodes.SUCCESS.name())) {
				orderResponse.setInventoryResponse(orderRequest.getInventoryResponse());
				orderResponse.setPaymentResponse(orderRequest.getPaymentResponse());
				orderResponse = inventoryService.callInventoryService(orderResponse);
				if (orderResponse.getInventoryResponse().equals(ResponseCodes.SUCCESS.name())) {
					orderResponse = paymentService.callPaymentService(orderResponse);
					if (orderResponse.getPaymentResponse().equals(ResponseCodes.SUCCESS.name())) {
						orderResponse.setFinalStatus(determineFinalStatus(orderResponse));
					} else {
						orderResponse.setFinalStatus(ResponseCodes.FAILURE.name());
						orderResponse.setErrorMessage("Payment Service Failed");
					}
				} else {
					orderResponse.setPaymentResponse(null);
					orderResponse.setFinalStatus(ResponseCodes.FAILURE.name());
					orderResponse.setErrorMessage("Inventory Service Failed");
				}
			} else {
				orderResponse.setInventoryResponse(null);
				orderResponse.setPaymentResponse(null);
				orderResponse.setFinalStatus(ResponseCodes.FAILURE.name());
				orderResponse.setErrorMessage("Order Service Failed");
			}

		}catch (Exception e){
			System.out.println(e);
		}finally {
			orderResponse.setReferenceId(httpRequestData.getReference());

			orderResponse.setOrderToInventorySendTime(orderResponse != null ? orderResponse.getOrderToInventorySendTime() : null);
			orderResponse.setOrderToInventoryReceivedTime(orderResponse != null ? orderResponse.getOrderToInventoryReceivedTime() : null);
			orderResponse.setInventoryToOrderSendTime(orderResponse != null ? orderResponse.getInventoryToOrderSendTime() : null);
			orderResponse.setInventoryToOrderReceivedTime(orderResponse != null ? orderResponse.getInventoryToOrderReceivedTime() : null);
			orderResponse.setOrderToPaymentSendTime(orderResponse != null ? orderResponse.getOrderToPaymentSendTime() : null);
			orderResponse.setOrderToPaymentReceivedTime(orderResponse != null ? orderResponse.getOrderToPaymentReceivedTime() : null);
			orderResponse.setPaymentToOrderSendTime(orderResponse != null ? orderResponse.getPaymentToOrderSendTime() : null);
			orderResponse.setPaymentToOrderReceivedTime(orderResponse != null ? orderResponse.getPaymentToOrderReceivedTime() : null);
			dataService.saveOrder(orderResponse);

		}
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
