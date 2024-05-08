package com.research.te04orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te04orderservice.constants.ResponseCodes;
import com.research.te04orderservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class InventoryService {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	@Value(value = "${spring.inventory.service.host}")
	private String inventoryServiceHost;
	public Response callInventoryService( Response orderResponse) throws JsonProcessingException {
			String orderToInventorySendTime = Instant.now().toString();
			orderResponse.setOrderToInventorySendTime(orderToInventorySendTime);
			String inventoryUrl = "http://"+inventoryServiceHost+":9060/inventory/check";
			String jsonRequest = objectMapper.writeValueAsString(orderResponse);
			Response inventoryResponse = restTemplate.postForObject(inventoryUrl, jsonRequest, Response.class);
			inventoryResponse.setInventoryToOrderReceivedTime(Instant.now().toString());
			return inventoryResponse;
	}
}
