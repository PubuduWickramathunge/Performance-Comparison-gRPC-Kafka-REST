package com.research.te02orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te02orderservice.constants.ResponseCodes;
import com.research.te02orderservice.data.service.DataService;
import com.research.te02orderservice.model.HttpRequestData;
import com.research.te02orderservice.model.OrderRequest;
import com.research.te02orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/")
@Slf4j
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DataService dataService;
	@PostMapping("ordercreate/{referenceId}")
	public ResponseEntity<OrderRequest> createOrder(
			@PathVariable String referenceId,
			@RequestBody String requestBody) throws JsonProcessingException {
		log.info("OrderController.createOrder()[POST] initiated | referenceId: {}", referenceId);
		HttpRequestData httpRequestData = new HttpRequestData(referenceId, null, requestBody);
		OrderRequest orderRequest = objectMapper.readValue(httpRequestData.getRequestBody(), OrderRequest.class);
		orderRequest.setReferenceId(referenceId);

		try {

			CompletableFuture<OrderRequest> orderProcessingFuture = orderService.processOrder(orderRequest);
			// Wait for the order processing to complete
			OrderRequest orderResponse = orderProcessingFuture.join();

			HttpStatus httpStatus = httpStatus(orderResponse);
			log.info("OrderController.createOrder() finished with status: {} | referenceId: {}", httpStatus, referenceId);
			dataService.saveOrder(orderResponse);
			return new ResponseEntity<>(orderResponse, httpStatus);
		} catch (Exception exception) {
			log.error("OrderController.createOrder() unexpected error occurred | referenceId: {}", referenceId, exception);
			orderRequest.setFinalStatus(ResponseCodes.FAILURE.name());
			orderRequest.setErrorMessage(exception.getMessage());
			dataService.saveOrder(orderRequest);
			return new ResponseEntity<>(orderRequest, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	private HttpStatus httpStatus(OrderRequest orderResponse) {
		if (orderResponse.getFinalStatus().equals(ResponseCodes.SUCCESS.name())) {
			return HttpStatus.OK;
		} else if (orderResponse.getFinalStatus().equals(ResponseCodes.FAILURE.name())) {
			return HttpStatus.BAD_REQUEST;
		} else {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
