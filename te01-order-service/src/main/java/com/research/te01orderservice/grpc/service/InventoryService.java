package com.research.te01orderservice.grpc.service;

import com.research.grpc.InventoryMessage;
import com.research.te01orderservice.grpc.client.InventoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class InventoryService {

	@Autowired
	private InventoryClient inventoryClient;
	public InventoryMessage callInventoryService( String inventoryStatus){
		String orderToInventorySendTime = Instant.now().toString();
		InventoryMessage inventoryRequestMessage = InventoryMessage.newBuilder().setInventoryStatus(inventoryStatus).setOrderToInventorySendTime(orderToInventorySendTime).build();
		return inventoryClient.checkInventory(inventoryRequestMessage);
	}

}
