package com.d205.KIWI_Backend.order.domain;

import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.order.domain.Order;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "KIOSK_ORDER")
public class KioskOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KIOSK_ID")
    private Kiosk kiosk;  // 키오스크와의 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;  // 주문과의 관계

    private LocalDateTime assignedTime;  // 주문이 키오스크에 배정된 시간

    // 연관관계 편의 메서드
    public void updateKiosk(Kiosk kiosk) {
        this.kiosk = kiosk;
        if (!kiosk.getKioskOrders().contains(this)) {
            kiosk.addKioskOrder(this);
        }
    }

    public void updateOrder(Order order) {
        this.order = order;
        if (!order.getKioskOrders().contains(this)) {
            order.addKioskOrder(this);
        }
    }
}
