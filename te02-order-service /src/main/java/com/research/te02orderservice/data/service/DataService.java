package com.research.te02orderservice.data.service;

import com.research.te02orderservice.data.Order;
import com.research.te02orderservice.data.repository.OrderRepository;
import com.research.te02orderservice.model.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class DataService {
	@Autowired
	private OrderRepository		 orderRepository;

	public void saveOrder(OrderRequest response) {
		Order order = new Order();
		order.setReferenceId(response.getReferenceId());
		order.setOrderStatus(response.getOrderResponse());
		order.setInventoryStatus(response.getInventoryResponse());
		order.setPaymentStatus(response.getPaymentResponse());
		order.setFinalStatus(response.getFinalStatus());
		order.setOrderToInventorySendTime(response.getOrderToInventorySendTime());
		order.setOrderToInventoryReceivedTime(response.getOrderToInventoryReceivedTime());
		order.setOrderToPaymentSendTime(response.getOrderToPaymentSendTime());
		order.setOrderToPaymentReceivedTime(response.getOrderToPaymentReceivedTime());
		order.setInventoryToOrderSendTime(response.getInventoryToOrderSendTime());
		order.setInventoryToOrderReceivedTime(response.getInventoryToOrderReceivedTime());
		order.setPaymentToOrderSendTime(response.getPaymentToOrderSendTime());
		order.setPaymentToOrderReceivedTime(response.getPaymentToOrderReceivedTime());

		order.setOrderToInventoryTime(calculateTimeDifference(response.getOrderToInventorySendTime(), response.getOrderToInventoryReceivedTime()));
		order.setOrderToPaymentTime(calculateTimeDifference(response.getOrderToPaymentSendTime(), response.getOrderToPaymentReceivedTime()));
		order.setInventoryToOrderTime(calculateTimeDifference(response.getInventoryToOrderSendTime(), response.getInventoryToOrderReceivedTime()));
		order.setPaymentToOrderTime(calculateTimeDifference(response.getPaymentToOrderSendTime(), response.getPaymentToOrderReceivedTime()));

		orderRepository.save(order);
	}

	private long calculateTimeDifference(String startTimeString, String endTimeString) {
		Instant startTime = Instant.parse(startTimeString);
		Instant endTime = Instant.parse(endTimeString);
		return Duration.between(startTime, endTime).toNanos();
	}
}
