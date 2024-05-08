package com.research.te01orderservice.grpc.client;

import com.research.grpc.PaymentMessage;
import com.research.grpc.PaymentServiceGrpc;
import com.research.te01orderservice.properties.ClientProperties;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentClient {
	private PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub;

	@Autowired
	private ClientProperties clientProperties;


	@PostConstruct
	private void init() {
		ManagedChannel channel = ManagedChannelBuilder
				.forAddress(clientProperties.getPaymentServiceHost(), clientProperties.getPaymentServicePort())
				.usePlaintext()
				.build();
		paymentStub = PaymentServiceGrpc.newBlockingStub(channel);
	}

	public PaymentMessage checkPayment(PaymentMessage paymentRequestMessage) {
		String orderToPaymentSendTime = Instant.now().toString();
		PaymentMessage requestMessage = PaymentMessage.newBuilder()
				.mergeFrom(paymentRequestMessage)
				.setOrderToPaymentSendTime(orderToPaymentSendTime)
				.build();

		PaymentMessage paymentResponseMessage = paymentStub.checkPayment(requestMessage);
		String orderToPaymentReceivedTime = Instant.now().toString();
		return PaymentMessage.newBuilder()
				.mergeFrom(paymentResponseMessage)
				.setPaymentToOrderReceivedTime(orderToPaymentReceivedTime)
				.build();
	}

}
