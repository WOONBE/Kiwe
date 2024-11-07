package com.d205.KIWI_Backend.order.repository;

import com.d205.KIWI_Backend.order.domain.Order;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Transactional
    @Query(value = "SELECT status FROM orders WHERE kiosk_id = :kioskId ORDER BY order_date DESC LIMIT 1", nativeQuery = true)
    String findLatestStatusByKioskId(@Param("kioskId") Long kioskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE orders o " +
            "JOIN (SELECT order_id FROM kiwi.kiosk_order WHERE kiosk_id = :kioskId ORDER BY order_id DESC LIMIT 1) latest_order " +
            "ON o.id = latest_order.order_id " +
            "SET o.status = 'COMPLETED'",
            nativeQuery = true)
    int updateOrderStatusToCompleted(@Param("kioskId") Long kioskId);

    @Query("SELECT o FROM Order o JOIN o.kioskOrders ko WHERE ko.kiosk.id = :kioskId AND o.orderDate > :oneMonthAgo")
    List<Order> findByKioskIdAndOrderDateAfter(@Param("kioskId") Integer kioskId, @Param("oneMonthAgo") LocalDateTime oneMonthAgo);

    @Query("SELECT o FROM Order o JOIN o.kioskOrders ko WHERE ko.kiosk.id = :kioskId AND o.orderDate BETWEEN :startOfMonth AND :endOfMonth")
    List<Order> findByKioskIdAndOrderDateBetween(@Param("kioskId") Integer kioskId, @Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);

    List<Order> findByOrderDateAfter(LocalDateTime date);

}
