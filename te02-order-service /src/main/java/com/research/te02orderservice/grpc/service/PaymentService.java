package com.research.te02orderservice.grpc.service;

import com.research.te02orderservice.kafka.KafkaPublisher;
import com.research.te02orderservice.kafka.PaymentStatusConsumer;
import com.research.te02orderservice.model.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {
	@Autowired
	private KafkaPublisher kafkaPublisher;

	@Autowired
	private PaymentStatusConsumer paymentStatusConsumer;

	@Value(value = "${spring.kafka.payment.topic}")
	private String paymentTopic;

	public CompletableFuture<OrderRequest> callPaymentService(OrderRequest orderRequest) {
		CompletableFuture<OrderRequest> paymentFuture = new CompletableFuture<>();
		orderRequest.setOrderToPaymentSendTime(Instant.now().toString());
		boolean isPublished = kafkaPublisher.sendMessage(paymentTopic, orderRequest);
		if (isPublished) {
			// Register a callback to handle the payment status response
			paymentStatusConsumer.registerCallback(paymentResponse -> {
				paymentFuture.complete(paymentResponse);
			});
		} else {
			paymentFuture.completeExceptionally(new Exception("Failed to publish payment request"));
		}

		return paymentFuture;
	}
}