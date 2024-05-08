package com.research.te01paymentservice.grpc.service;

import com.research.grpc.PaymentMessage;
import com.research.grpc.PaymentServiceGrpc;
import com.research.te01paymentservice.constants.RequestCodes;
import com.research.te01paymentservice.constants.ResponseCodes;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.time.Instant;

@GRpcService
public class PaymentService extends PaymentServiceGrpc.PaymentServiceImplBase {


	@Override
	public void checkPayment(PaymentMessage paymentRequestMessage, StreamObserver<PaymentMessage> responseObserver) {
		String paymentStatus;

		String paymentToOrderReceivedTime = Instant.now().toString();
		PaymentMessage requestMessage = PaymentMessage.newBuilder()
				.mergeFrom(paymentRequestMessage)
				.setOrderToPaymentReceivedTime(paymentToOrderReceivedTime)
				.build();
			if (RequestCodes.AVAILABLE.name().equals(requestMessage.getPaymentStatus())) {
				paymentStatus = ResponseCodes.SUCCESS.name();
			} else if (RequestCodes.UNAVAILABLE.name().equals(requestMessage.getPaymentStatus())) {
				paymentStatus = ResponseCodes.FAILURE.name();
			} else {
				paymentStatus = ResponseCodes.ERROR.name();
			}
		String paymentToOrderSendTime = Instant.now().toString();
		PaymentMessage response = PaymentMessage.newBuilder().mergeFrom(requestMessage).setPaymentStatus(paymentStatus)
				.setPaymentToOrderSendTime(paymentToOrderSendTime).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

}
