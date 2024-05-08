package com.research.te01orderservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HttpRequestData {
		private String reference;
		private Map<String, String> headers;
		private String requestBody;

	public HttpRequestData( String referenceId, Map<String, String> headers, String requestBody) {
		this.reference = referenceId;
		this.headers = headers;
		this.requestBody = requestBody;
	}

}
