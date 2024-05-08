package com.research.te01inventoryservice.grpc.service;

import com.research.grpc.InventoryMessage;
import com.research.grpc.InventoryServiceGrpc;
import com.research.te01inventoryservice.constants.RequestCodes;
import com.research.te01inventoryservice.constants.ResponseCodes;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.time.Instant;

@GRpcService
public class InventoryService extends InventoryServiceGrpc.InventoryServiceImplBase {


	@Override
	public void checkInventory(InventoryMessage inventoryRequestMessage, StreamObserver<InventoryMessage> responseObserver) {
		String inventoryStatus;

		String orderToInventoryReceivedTime = Instant.now().toString();
		InventoryMessage requestMessage = InventoryMessage.newBuilder()
				.mergeFrom(inventoryRequestMessage)
				.setOrderToInventoryReceivedTime(orderToInventoryReceivedTime)
				.build();

			if (RequestCodes.AVAILABLE.name().equals(requestMessage.getInventoryStatus())) {
				inventoryStatus = ResponseCodes.SUCCESS.name();
			} else if (RequestCodes.UNAVAILABLE.name().equals(requestMessage.getInventoryStatus())) {
				inventoryStatus = ResponseCodes.FAILURE.name();
			} else {
				inventoryStatus = ResponseCodes.ERROR.name();
			}
		String inventoryToOrderSendTime = Instant.now().toString();
		InventoryMessage response = InventoryMessage.newBuilder().mergeFrom(requestMessage).setInventoryStatus(inventoryStatus).setInventoryToOrderSendTime(inventoryToOrderSendTime).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

}
