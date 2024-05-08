package com.research.te02orderservice.grpc.service;

import com.research.te02orderservice.kafka.KafkaPublisher;
import com.research.te02orderservice.kafka.InventoryStatusConsumer;
import com.research.te02orderservice.model.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
public class InventoryService {
	@Autowired
	private KafkaPublisher kafkaPublisher;
	@Autowired
	private InventoryStatusConsumer inventoryStatusConsumer;
	@Value(value = "${spring.kafka.inventory.topic}")
	private String inventoryTopic;

	public CompletableFuture<OrderRequest> callInventoryService(OrderRequest orderRequest) {
		CompletableFuture<OrderRequest> inventoryFuture = new CompletableFuture<>();

		orderRequest.setOrderToInventorySendTime(Instant.now().toString());
		boolean isPublished = kafkaPublisher.sendMessage(inventoryTopic, orderRequest);
		if (isPublished) {
			inventoryStatusConsumer.registerCallback(inventoryFuture::complete);
		} else {
			inventoryFuture.completeExceptionally(new Exception("Failed to publish inventory request"));
		}
		return inventoryFuture;
	}
}