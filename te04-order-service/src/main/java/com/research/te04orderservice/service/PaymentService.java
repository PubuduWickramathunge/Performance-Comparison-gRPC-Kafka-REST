package com.research.te04orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te04orderservice.constants.ResponseCodes;
import com.research.te04orderservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class PaymentService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	@Value(value = "${spring.payment.service.host}")
	private String paymentServiceHost;
	public Response callPaymentService( Response orderResponse) throws Exception {
		String orderToPaymentSendTime = Instant.now().toString();
		orderResponse.setOrderToPaymentSendTime(orderToPaymentSendTime);
		String paymentUrl = "http://" + paymentServiceHost + ":9061/payment/check";
		String jsonRequest = objectMapper.writeValueAsString(orderResponse);
		Response paymentResponse = restTemplate.postForObject(paymentUrl, jsonRequest, Response.class);
		paymentResponse.setPaymentToOrderReceivedTime(Instant.now().toString());
		return paymentResponse;
	}

}
