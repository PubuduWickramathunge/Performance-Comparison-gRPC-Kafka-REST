package com.research.te01orderservice.grpc.client;

import com.research.grpc.InventoryMessage;
import com.research.grpc.InventoryServiceGrpc;
import com.research.te01orderservice.properties.ClientProperties;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InventoryClient {
	private InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub;

	@Autowired
	private ClientProperties clientProperties;

	@PostConstruct
	private void init() {
		ManagedChannel channel = ManagedChannelBuilder
				.forAddress(clientProperties.getInventoryServiceHost(), clientProperties.getInventoryServicePort())
				.usePlaintext()
				.build();
		inventoryStub = InventoryServiceGrpc.newBlockingStub(channel);
	}

	public InventoryMessage checkInventory(InventoryMessage inventoryRequestMessage) {
		InventoryMessage inventoryResponseMessage = inventoryStub.checkInventory(inventoryRequestMessage);
		String inventoryToOrderReceivedTime = Instant.now().toString();
		return InventoryMessage.newBuilder()
				.mergeFrom(inventoryResponseMessage)
				.setInventoryToOrderReceivedTime(inventoryToOrderReceivedTime)
				.build();
	}

}
