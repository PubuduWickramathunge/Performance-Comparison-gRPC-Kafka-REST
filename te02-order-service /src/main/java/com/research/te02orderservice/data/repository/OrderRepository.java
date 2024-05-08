package com.research.te02orderservice.data.repository;

import com.research.te02orderservice.data.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
