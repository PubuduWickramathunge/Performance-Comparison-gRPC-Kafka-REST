package com.research.te01orderservice.data.repository;

import com.research.te01orderservice.data.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
