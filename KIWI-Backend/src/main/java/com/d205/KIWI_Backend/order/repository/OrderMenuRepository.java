package com.d205.KIWI_Backend.order.repository;

import com.d205.KIWI_Backend.order.domain.OrderMenu;
import com.d205.KIWI_Backend.order.dto.PaymentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {

    @Query(value = """
        SELECT 
            m.menu_category AS menuCategory, 
            m.menu_name AS menuName, 
            m.menu_price AS menuPrice, 
            om.quantity AS quantity, 
            (m.menu_price * om.quantity) AS totalPrice
        FROM order_menu om
        JOIN menu m ON m.menu_id = om.menu_id
        WHERE om.order_id = (
            SELECT order_id 
            FROM kiosk_order ko
            WHERE ko.kiosk_id = :kioskId
            ORDER BY id DESC
            LIMIT 1
        )
    """, nativeQuery = true)
    List<PaymentResult> findLatestOrderMenu(@Param("kioskId") int kioskId);
}
