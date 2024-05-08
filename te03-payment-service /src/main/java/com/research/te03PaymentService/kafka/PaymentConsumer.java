package com.research.te03PaymentService.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.te03PaymentService.constants.RequestCodes;
import com.research.te03PaymentService.constants.ResponseCodes;
import com.research.te03PaymentService.model.OrderRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentConsumer {

	@Autowired
	private KafkaPublisher kafkaPublisher;
	@Autowired
	private ObjectMapper objectMapper;

	@Value(value = "${spring.kafka.payment.status.topic}")
	private String paymentStatusTopic;
	@KafkaListener(topics = "te03ordertopic", groupId = "paymentgroup")
	public void paymentListener(ConsumerRecord<String, String> record) throws JsonProcessingException {
		System.out.println(record);
		String messageJson = record.value();
		OrderRequest orderRequest = objectMapper.readValue(messageJson, OrderRequest.class);
		orderRequest.setOrderToPaymentReceivedTime(Instant.now().toString());
		if (orderRequest.getPaymentResponse().equals(RequestCodes.AVAILABLE.name())){
			orderRequest.setPaymentResponse(ResponseCodes.SUCCESS.name());
		}else {
			orderRequest.setPaymentResponse(ResponseCodes.FAILURE.name());
		}
		orderRequest.setPaymentToOrderSendTime(Instant.now().toString());
		kafkaPublisher.sendMessage(paymentStatusTopic, orderRequest);
	}
}
