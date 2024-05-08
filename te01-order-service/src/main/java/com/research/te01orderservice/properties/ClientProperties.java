package com.research.te01orderservice.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ClientProperties {
	@Value("${research.te01-inventory-service.grpc.client.host}")
	private String inventoryServiceHost;

	@Value("${research.te01-inventory-service.grpc.client.port}")
	private int inventoryServicePort;

	@Value("${research.te01-payment-service.grpc.client.host}")
	private String paymentServiceHost;

	@Value("${research.te01-payment-service.grpc.client.port}")
	private int paymentServicePort;
}
