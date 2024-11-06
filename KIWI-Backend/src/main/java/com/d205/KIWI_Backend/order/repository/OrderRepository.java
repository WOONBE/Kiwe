package com.d205.KIWI_Backend.order.repository;

import com.d205.KIWI_Backend.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
