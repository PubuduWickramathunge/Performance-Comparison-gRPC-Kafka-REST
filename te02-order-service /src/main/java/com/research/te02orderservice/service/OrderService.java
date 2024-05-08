package com.research.te02orderservice.service;

import com.research.te02orderservice.constants.RequestCodes;
import com.research.te02orderservice.constants.ResponseCodes;
import com.research.te02orderservice.grpc.service.InventoryService;
import com.research.te02orderservice.grpc.service.PaymentService;
import com.research.te02orderservice.model.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private PaymentService paymentService;
	public CompletableFuture<OrderRequest> processOrder(OrderRequest orderRequest) {
		CompletableFuture<OrderRequest> orderFuture =
				handleOrderResponse(orderRequest)
						.thenCompose(this::callInventoryService)
						.thenCompose(this::handleInventoryResponse)
						.thenCompose(this::callPaymentService)
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
	private CompletableFuture<OrderRequest> callInventoryService(OrderRequest orderRequest) {
		return inventoryService.callInventoryService(orderRequest);
	}
	private CompletableFuture<OrderRequest> handleInventoryResponse(OrderRequest orderRequest) {
		if (!orderRequest.getInventoryResponse().equals(ResponseCodes.SUCCESS.name())) {
			return CompletableFuture.failedFuture(new Exception("Inventory service failed"));
		} else {
			return CompletableFuture.completedFuture(orderRequest);
		}
	}
	private CompletableFuture<OrderRequest> callPaymentService(OrderRequest orderRequest) {
		return paymentService.callPaymentService(orderRequest);
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