package com.research.te03orderservice.data.repository;

import com.research.te03orderservice.data.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
