package com.research.te04inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te04inventoryservice.service.InventoryService;
import com.research.te04inventoryservice.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/")
@Slf4j
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("inventory/check")
	public Response checkInventory(
			@RequestBody String requestBody) throws JsonProcessingException {
		log.info("InventoryController.checkInventory()[POST] initiated");
			Response response = objectMapper.readValue(requestBody, Response.class);
			response.setOrderToInventoryReceivedTime(Instant.now().toString());
			inventoryService.checkInventory(response);
		response.setInventoryToOrderSendTime(Instant.now().toString());
		log.info("InventoryController.checkInventory()[POST] completed");
		return response;
	}
	}