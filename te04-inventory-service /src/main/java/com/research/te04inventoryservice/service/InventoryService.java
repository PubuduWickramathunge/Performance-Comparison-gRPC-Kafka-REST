package com.research.te04inventoryservice.service;

import com.research.te04inventoryservice.constants.RequestCodes;
import com.research.te04inventoryservice.constants.ResponseCodes;
import com.research.te04inventoryservice.model.Response;
import org.springframework.stereotype.Service;


@Service
public class InventoryService  {



	public void checkInventory(Response response) {

			if (RequestCodes.AVAILABLE.name().equals(response.getInventoryResponse())) {
				response.setInventoryResponse(ResponseCodes.SUCCESS.name());
			} else if (RequestCodes.UNAVAILABLE.name().equals(response.getInventoryResponse())) {
				response.setInventoryResponse(ResponseCodes.FAILURE.name());
			} else {
				response.setInventoryResponse(ResponseCodes.ERROR.name());
			}
	}

}
