package com.research.te03orderservice.grpc.service;

import com.research.te03orderservice.kafka.InventoryStatusConsumer;
import com.research.te03orderservice.kafka.KafkaPublisher;
import com.research.te03orderservice.kafka.PaymentStatusConsumer;
import com.research.te03orderservice.model.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Service
public class KafkaPublishingService {
	@Autowired
	private KafkaPublisher kafkaPublisher;
	@Autowired
	private InventoryStatusConsumer inventoryStatusConsumer;
	@Autowired
	private PaymentStatusConsumer paymentStatusConsumer;
	@Value(value = "${spring.kafka.topic}")
	private String kafkaTopic;

	public CompletableFuture<OrderRequest> publishMessage(OrderRequest orderRequest) {
		CompletableFuture<OrderRequest> messageFuture = new CompletableFuture<>();
		CountDownLatch countDownLatch = new CountDownLatch(2);

		orderRequest.setOrderToInventorySendTime(Instant.now().toString());
		orderRequest.setOrderToPaymentSendTime(Instant.now().toString());

		boolean isPublished = kafkaPublisher.sendMessage(kafkaTopic, orderRequest);
		if (isPublished) {
			registerCallbacks(orderRequest, countDownLatch, messageFuture);
		} else {
			messageFuture.completeExceptionally(new Exception("Failed to publish request"));
		}
		return messageFuture;
	}

	private void registerCallbacks(OrderRequest orderRequest, CountDownLatch countDownLatch, CompletableFuture<OrderRequest> messageFuture) {
		inventoryStatusConsumer.registerCallback(orderRequest, updatedOrderRequest -> {
			orderRequest.setInventoryResponse(updatedOrderRequest.getInventoryResponse());
			orderRequest.setInventoryToOrderReceivedTime(updatedOrderRequest.getInventoryToOrderReceivedTime());
			countDownLatch.countDown();
			if (countDownLatch.getCount() == 0) {
				messageFuture.complete(orderRequest);
			}
		});

		paymentStatusConsumer.registerCallback(orderRequest, updatedOrderRequest -> {
			orderRequest.setPaymentResponse(updatedOrderRequest.getPaymentResponse());
			orderRequest.setPaymentToOrderReceivedTime(updatedOrderRequest.getPaymentToOrderReceivedTime());
			countDownLatch.countDown();
			if (countDownLatch.getCount() == 0) {
				messageFuture.complete(orderRequest);
			}
		});
	}
}