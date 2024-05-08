package com.research.te04orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EmployeeConfig {

	// Other Beans

	@Bean
	public RestTemplate restTemplateBean() {
		return new RestTemplate();
	}

	// Other Beans

}
