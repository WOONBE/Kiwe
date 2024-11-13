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

    @Query("SELECT o FROM Order o JOIN o.kioskOrders ko WHERE ko.kiosk.id = :kioskId AND o.orderDate > :oneMonthAgo")
    List<Order> findByKioskIdAndOrderDateAfter(@Param("kioskId") Integer kioskId, @Param("oneMonthAgo") LocalDateTime oneMonthAgo);

    @Query("SELECT o FROM Order o JOIN o.kioskOrders ko WHERE ko.kiosk.id = :kioskId AND o.orderDate BETWEEN :startOfMonth AND :endOfMonth")
    List<Order> findByKioskIdAndOrderDateBetween(@Param("kioskId") Integer kioskId, @Param("startOfMonth") LocalDateTime startOfMonth, @Param("endOfMonth") LocalDateTime endOfMonth);

    List<Order> findByOrderDateAfter(LocalDateTime date);

    @Transactional
    @Query(value = "SELECT o.status FROM ORDERS o " +
        "JOIN KIOSK_ORDER k ON o.id = k.ORDER_ID " +
        "WHERE k.KIOSK_ID = :kioskId " +
        "ORDER BY o.ORDER_DATE DESC LIMIT 1", nativeQuery = true)
    String findLatestStatusByKioskId(@Param("kioskId") Long kioskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ORDERS o " +
        "JOIN (SELECT order_id FROM KIOSK_ORDER WHERE kiosk_id = :kioskId ORDER BY order_id DESC LIMIT 1) latest_order " +
        "ON o.id = latest_order.order_id " +
        "SET o.status = 'COMPLETED'", nativeQuery = true)
    int updateOrderStatusToCompleted(@Param("kioskId") Long kioskId);

    @Transactional
    @Query(value = "SELECT o.id FROM orders o " +
        "JOIN kiosk_order ko ON o.id = ko.order_id " +
        "WHERE ko.kiosk_id = :kioskId " +
        "ORDER BY o.order_date DESC LIMIT 1", nativeQuery = true)
    Long findLatestOrderIdByKioskId(@Param("kioskId") Long kioskId);

    @Query("SELECT o FROM Order o JOIN o.kioskOrders ko WHERE ko.kiosk.id = :kioskId" )
    List<Order> findByKioskId(Integer kioskId);


    @Query("SELECT m.name AS menuName, SUM(om.quantity) AS totalSales, o.age " +
        "FROM Order o " +
        "JOIN o.orderMenus om " +
        "JOIN om.menu m " +
        "JOIN o.kioskOrders ko " +
        "WHERE ko.kiosk.id = :kioskId " +
        "AND o.orderDate BETWEEN :startDate AND :endDate " +
        "GROUP BY m.name, o.age " +
        "ORDER BY totalSales DESC")
    List<Object[]> findTopSellingMenusByAgeGroupByKioskId(
        @Param("kioskId") Integer kioskId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m.name AS menuName, SUM(om.quantity) AS totalSales, o.age " +
        "FROM Order o " +
        "JOIN o.orderMenus om " +
        "JOIN om.menu m " +
        "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
        "GROUP BY m.name, o.age " +
        "ORDER BY totalSales DESC")
    List<Object[]> findTopSellingMenusByAgeGroup(@Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o " +
        "JOIN KioskOrder ko ON o.id = ko.order.id " +
        "JOIN Kiosk k ON ko.kiosk.id = k.id " +
        "WHERE k.member.id = :memberId")
    List<Order> findOrdersByMemberId(@Param("memberId") Integer memberId);


//    @Query("""
//        SELECT new com.d205.KIWI_Backend.order.dto.AgeGroupTopMenuResponse(
//            o.age AS ageGroup,
//            om.menu.id AS mostSoldMenuId,
//            SUM(om.quantity) AS totalQuantity
//        FROM KioskOrder ko
//        JOIN ko.order o
//        JOIN o.orderMenus om
//        WHERE ko.kiosk.id IN (
//            SELECT k.id FROM Kiosk k WHERE k.owner.id = :memberId
//        )
//        GROUP BY o.age, om.menu.id
//        HAVING SUM(om.quantity) = (
//            SELECT MAX(sub.totalQuantity)
//            FROM (
//                SELECT SUM(om2.quantity) AS totalQuantity
//                FROM KioskOrder ko2
//                JOIN ko2.order o2
//                JOIN o2.orderMenus om2
//                WHERE ko2.kiosk.id IN (
//                    SELECT k.id FROM Kiosk k WHERE k.owner.id = :memberId
//                )
//                AND o2.age = o.age
//                GROUP BY om2.menu.id
//            ) AS sub
//        )
//        ORDER BY o.age
//    """)
//    List<AgeGroupTopMenuResponse> findTopMenuByAgeGroupForMember(@Param("memberId") Long memberId);



//
//    @Query("SELECT m.name, SUM(om.quantity) AS totalSales " +
//        "FROM Order o " +
//        "JOIN o.orderMenus om " +
//        "JOIN om.menu m " +
//        "WHERE o.age = :age AND o.orderDate BETWEEN :startDate AND :endDate " +
//        "GROUP BY m.name " +
//        "ORDER BY totalSales DESC")
//    List<Object[]> findTopSellingMenusForAgeGroup(@Param("age") String age,
//        @Param("startDate") LocalDateTime startDate,
//        @Param("endDate") LocalDateTime endDate);
//
//
//    @Query("SELECT om.menu.name, SUM(om.quantity) AS totalSales " +
//        "FROM Order o " +
//        "JOIN o.orderMenus om " +
//        "JOIN o.kioskOrders ko " +
//        "WHERE ko.kiosk.id = :kioskId " +
//        "AND o.age = :age " +
//        "AND o.orderDate BETWEEN :startDate AND :endDate " +
//        "GROUP BY om.menu.name " +
//        "ORDER BY totalSales DESC")
//    List<Object[]> findTopSellingMenusForAgeGroupAndKiosk(
//        @Param("kioskId") Integer kioskId,
//        @Param("age") String age,
//        @Param("startDate") LocalDateTime startDate,
//        @Param("endDate") LocalDateTime endDate);





}
