package com.research.te04orderservice.controller;

import com.research.te04orderservice.constants.RequestCodes;
import com.research.te04orderservice.constants.ResponseCodes;
import com.research.te04orderservice.model.HttpRequestData;
import com.research.te04orderservice.model.Response;
import com.research.te04orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("ordercreate/{referenceId}")
	public ResponseEntity<Response> createOrder(
			@PathVariable String referenceId,
			@RequestBody String requestBody) {
		try {
			log.info("OrderController.createOrder()[POST] initiated | referenceId: {}", referenceId);
			HttpRequestData httpRequestData = new HttpRequestData(referenceId, null, requestBody);
			Response orderResponse = orderService.createOrder(httpRequestData);

			HttpStatus httpStatus = httpStatus(orderResponse);
			log.info("OrderController.createOrder() finished with status: {} | referenceId: {}", httpStatus, referenceId);
			return new ResponseEntity<>(orderResponse, httpStatus);
		} catch (Exception exception) {
			log.error("OrderController.createOrder() unexpected error occurred referenceId: {} | exception: {}", referenceId, exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private HttpStatus httpStatus(Response orderResponse) {
		if (orderResponse.getFinalStatus().equals(ResponseCodes.SUCCESS.name())) {
			return HttpStatus.OK;
		} else if (orderResponse.getFinalStatus().equals(ResponseCodes.FAILURE.name())) {
			return HttpStatus.BAD_REQUEST;
		} else {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
