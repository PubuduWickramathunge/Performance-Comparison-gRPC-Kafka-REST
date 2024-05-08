package com.research.te04paymentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te04paymentservice.model.Response;
import com.research.te04paymentservice.service.PaymentService;
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
public class PaymentController {
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private ObjectMapper objectMapper;
	@PostMapping("payment/check")
	public Response checkPayment(@RequestBody String requestBody) throws JsonProcessingException {
		log.info("InventoryController.checkInventory()[POST] initiated");
		Response response = objectMapper.readValue(requestBody, Response.class);
		response.setOrderToPaymentReceivedTime(Instant.now().toString());
		paymentService.checkPayment(response);
		response.setPaymentToOrderSendTime(Instant.now().toString());
		log.info("InventoryController.checkInventory()[POST] completed");
		return response;
	}
}