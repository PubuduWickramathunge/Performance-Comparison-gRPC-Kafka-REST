package com.research.te02orderservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te02orderservice.model.OrderRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.function.Consumer;

@Component
public class InventoryStatusConsumer {
	private Consumer<OrderRequest> inventoryStatusCallback;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = "${spring.kafka.inventory.status.topic}", groupId = "kafkagroup")
	public void consumeInventoryStatusMessage(ConsumerRecord<String, String> record) throws JsonProcessingException {
		String messageJson = record.value();
		OrderRequest orderRequest = objectMapper.readValue(messageJson, OrderRequest.class);
		orderRequest.setInventoryToOrderReceivedTime(Instant.now().toString());
		if (inventoryStatusCallback != null) {
			inventoryStatusCallback.accept(orderRequest);
		}
	}

	public void registerCallback(Consumer<OrderRequest> callback) {
		this.inventoryStatusCallback = callback;
	}
}
