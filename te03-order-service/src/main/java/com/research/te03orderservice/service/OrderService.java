package com.research.te03orderservice.service;

import com.research.te03orderservice.constants.RequestCodes;
import com.research.te03orderservice.constants.ResponseCodes;
import com.research.te03orderservice.grpc.service.KafkaPublishingService;
import com.research.te03orderservice.model.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

	@Autowired
	private KafkaPublishingService kafkaPublishingService;

	public CompletableFuture<OrderRequest> processOrder(OrderRequest orderRequest) {
		CompletableFuture<OrderRequest> orderFuture =
				handleOrderResponse(orderRequest)
						.thenCompose(this::callOtherServices)
						.thenCompose(this::handleInventoryResponse)
						.thenCompose(this::handlePaymentResponse)
						.thenApply(this::determineFinalStatus);

		orderFuture.whenComplete((result, ex) -> {
			if (ex != null) {
				orderFuture.completeExceptionally(ex);
			}
		});

		return orderFuture;
	}
	private CompletableFuture<OrderRequest> handleOrderResponse(OrderRequest orderRequest) {
		if (orderRequest.getOrderResponse().equals(RequestCodes.UNAVAILABLE.name())) {
			return CompletableFuture.failedFuture(new Exception("order service failed"));
		} else {
			orderRequest.setOrderResponse(ResponseCodes.SUCCESS.name());
			return CompletableFuture.completedFuture(orderRequest);
		}
	}

	private CompletableFuture<OrderRequest> callOtherServices(OrderRequest orderRequest) {

		return kafkaPublishingService.publishMessage(orderRequest);
	}
	private CompletableFuture<OrderRequest> handleInventoryResponse(OrderRequest orderRequest) {
		if (!orderRequest.getInventoryResponse().equals(ResponseCodes.SUCCESS.name())) {
			return CompletableFuture.failedFuture(new Exception("Inventory service failed"));
		} else {
			return CompletableFuture.completedFuture(orderRequest);
		}
	}


	private CompletableFuture<OrderRequest> handlePaymentResponse(OrderRequest orderRequest) {
		if (!orderRequest.getPaymentResponse().equals(ResponseCodes.SUCCESS.name())) {
			return CompletableFuture.failedFuture(new Exception("Payment service failed"));
		} else {
			return CompletableFuture.completedFuture(orderRequest);
		}
	}

	private OrderRequest determineFinalStatus(OrderRequest orderResponse) {
		if (orderResponse.getOrderResponse().equals(ResponseCodes.SUCCESS.name()) &&
				orderResponse.getInventoryResponse().equals(ResponseCodes.SUCCESS.name()) &&
				orderResponse.getPaymentResponse().equals(ResponseCodes.SUCCESS.name())) {
			orderResponse.setFinalStatus(ResponseCodes.SUCCESS.name());

		} else {
			orderResponse.setFinalStatus(ResponseCodes.FAILURE.name());
		}
		return orderResponse;
	}


}