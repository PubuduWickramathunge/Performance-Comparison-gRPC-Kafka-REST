package com.research.te01orderservice.grpc.service;

import com.research.grpc.PaymentMessage;
import com.research.te01orderservice.grpc.client.PaymentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentService {

	@Autowired
	private PaymentClient paymentClient;
	public PaymentMessage callPaymentService( String paymentStatus){

		PaymentMessage paymentRequestMessage = PaymentMessage.newBuilder().setPaymentStatus(paymentStatus).build();
		return paymentClient.checkPayment(paymentRequestMessage);
	}

}
