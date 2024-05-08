package com.research.te03InventoryService.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te03InventoryService.constants.RequestCodes;
import com.research.te03InventoryService.constants.ResponseCodes;
import com.research.te03InventoryService.model.OrderRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InventoryConsumer {

	@Autowired
	private KafkaPublisher kafkaPublisher;
	@Autowired
	private ObjectMapper objectMapper;

	@Value(value = "${spring.kafka.inventory.status.topic}")
	private String inventoryStatusTopic;
	@KafkaListener(topics = "te03ordertopic", groupId = "inventorygroup")
	public void inventoryListener(ConsumerRecord<String, String> record) throws JsonProcessingException {
		System.out.println(record);
		String messageJson = record.value();
		OrderRequest orderRequest = objectMapper.readValue(messageJson, OrderRequest.class);
		orderRequest.setOrderToInventoryReceivedTime(Instant.now().toString());
		if (orderRequest.getInventoryResponse().equals(RequestCodes.AVAILABLE.name())){
			orderRequest.setInventoryResponse(ResponseCodes.SUCCESS.name());
		}else {
			orderRequest.setInventoryResponse(ResponseCodes.FAILURE.name());
		}
		orderRequest.setInventoryToOrderSendTime(Instant.now().toString());
		kafkaPublisher.sendMessage(inventoryStatusTopic, orderRequest);
	}
}
