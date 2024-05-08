package com.research.te04orderservice.data.repository;

import com.research.te04orderservice.data.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
