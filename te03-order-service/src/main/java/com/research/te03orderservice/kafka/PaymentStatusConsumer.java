package com.research.te03orderservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te03orderservice.model.OrderRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.function.Consumer;

@Component
public class PaymentStatusConsumer {
	private Consumer<OrderRequest> paymentStatusCallback;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = "${spring.kafka.payment.status.topic}", groupId = "kafkagroup")
	public void consumePaymentStatusMessage(ConsumerRecord<String, String> record) throws JsonProcessingException {
		String messageJson = record.value();
		OrderRequest orderRequest = objectMapper.readValue(messageJson, OrderRequest.class);
		orderRequest.setPaymentToOrderReceivedTime(Instant.now().toString());

		if (paymentStatusCallback != null) {
			paymentStatusCallback.accept(orderRequest);
		}
	}

	public void registerCallback(OrderRequest originalOrderRequest, Consumer<OrderRequest> callback) {
		Consumer<OrderRequest> orderRequestConsumer = updatedOrderRequest -> {
			updatedOrderRequest.setPaymentToOrderReceivedTime(Instant.now().toString());
			callback.accept(updatedOrderRequest);
			originalOrderRequest.setPaymentResponse(updatedOrderRequest.getPaymentResponse());
			originalOrderRequest.setPaymentToOrderReceivedTime(updatedOrderRequest.getPaymentToOrderReceivedTime());
			originalOrderRequest.setOrderToPaymentReceivedTime(updatedOrderRequest.getOrderToPaymentReceivedTime());
			originalOrderRequest.setPaymentToOrderSendTime(updatedOrderRequest.getPaymentToOrderSendTime());
		};
		this.paymentStatusCallback = orderRequestConsumer;
	}
}