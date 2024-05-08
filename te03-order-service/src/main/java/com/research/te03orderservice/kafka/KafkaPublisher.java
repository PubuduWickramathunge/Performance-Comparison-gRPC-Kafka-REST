package com.research.te03orderservice.kafka;

import com.research.te03orderservice.model.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaPublisher {
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;


	public boolean sendMessage(String topicName, OrderRequest message) {
		boolean isPublished = false;
		try{
			send(topicName, message);
			isPublished = true;
		}catch (Exception exception){
			log.error("KafkaPublisher.sendMessage() an error occurred while publishing the request for topic : {}", topicName, exception);
		}
		return isPublished;
	}

	private void send(String topicName, OrderRequest message) {
		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, message);
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				log.info("KafkaPublisher.sendMessage() successfully publishing the request for topic : {}", topicName);
			} else {
				throw new KafkaException("Exception occurred when publishing message");
			}
		});
	}
}
