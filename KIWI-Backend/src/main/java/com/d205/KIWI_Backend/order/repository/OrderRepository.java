package com.d205.KIWI_Backend.order.repository;

import com.d205.KIWI_Backend.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.status FROM Order o WHERE o.kioskId = :kioskId ORDER BY o.orderDate DESC")
    String findLatestStatusByKioskId(@Param("kioskId") Long kioskId);
}
