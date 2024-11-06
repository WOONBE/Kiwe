package com.d205.KIWI_Backend.order.domain;

import com.d205.KIWI_Backend.menu.domain.Menu;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class OrderMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID")
    private Menu menu;

    private Integer quantity;

    public void updateOrder(Order order) {
        this.order = order;
        if (!order.getOrderMenus().contains(this)) {
            order.addOrderMenu(this);
        }
    }

    public void updateMenu(Menu menu) {
        this.menu = menu;
        if (!menu.getOrderMenus().contains(this)) {
            menu.addOrderMenu(this);
        }
    }
}
