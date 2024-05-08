package com.research.te04orderservice.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "order")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "referenceId")
	private String referenceId;

	@Column(name = "orderStatus")
	private String orderStatus;
	@Column(name = "inventoryStatus")
	private String inventoryStatus;
	@Column(name = "paymentStatus")
	private String paymentStatus;
	@Column(name = "finalStatus")
	private String finalStatus;
	@Column(name = "OrderToInventorySendTime")
	private String orderToInventorySendTime;
	@Column(name = "OrderToInventoryReceivedTime")
	private String orderToInventoryReceivedTime;
	@Column(name = "OrderToPaymentSendTime")
	private String orderToPaymentSendTime;
	@Column(name = "OrderToPaymentReceivedTime")
	private String orderToPaymentReceivedTime;
	@Column(name = "InventoryToOrderSendTime")
	private String inventoryToOrderSendTime;
	@Column(name = "InventoryToOrderReceivedTime")
	private String inventoryToOrderReceivedTime;
	@Column(name = "PaymentToOrderSendTime")
	private String paymentToOrderSendTime;
	@Column(name = "PaymentToOrderReceivedTime")
	private String paymentToOrderReceivedTime;

	@Column(name = "OrderToInventoryTime")
	private Long orderToInventoryTime;
	@Column(name = "OrderToPaymentTime")
	private Long orderToPaymentTime;
	@Column(name = "InventoryToOrderTime")
	private Long inventoryToOrderTime;
	@Column(name = "PaymentToOrderTime")
	private Long paymentToOrderTime;
}