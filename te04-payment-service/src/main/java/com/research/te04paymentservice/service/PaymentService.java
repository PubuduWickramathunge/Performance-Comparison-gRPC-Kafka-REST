package com.research.te04paymentservice.service;

import com.research.te04paymentservice.constants.RequestCodes;
import com.research.te04paymentservice.constants.ResponseCodes;
import com.research.te04paymentservice.model.Response;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {



	public void checkPayment(Response response) {

			if (RequestCodes.AVAILABLE.name().equals(response.getPaymentResponse())) {
				response.setPaymentResponse(ResponseCodes.SUCCESS.name());
			} else if (RequestCodes.UNAVAILABLE.name().equals(response.getPaymentResponse())) {
				response.setPaymentResponse(ResponseCodes.FAILURE.name());
			} else {
				response.setPaymentResponse(ResponseCodes.ERROR.name());
			}
	}

}
