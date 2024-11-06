package com.d205.KIWI_Backend.order.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderMenu> orderMenus =  new ArrayList<>();;

    // 연관관계 편의 메서드
    public void addOrderMenu(OrderMenu orderMenu) {
        this.orderMenus.add(orderMenu);
        if (orderMenu.getOrder() != this) {
            orderMenu.updateOrder(this);
        }
    }

    public void addOrderMenus(List<OrderMenu> newOrderMenus) {
        if (this.orderMenus == null) {
            this.orderMenus = new ArrayList<>();
        }
        this.orderMenus.addAll(newOrderMenus); // 새로운 메뉴 항목들을 주문에 추가
    }

    public void clearOrderMenus() {
        if (this.orderMenus != null) {
            this.orderMenus.clear(); // Order에 연관된 모든 메뉴 항목 제거
        }
    }
    // 기존 메뉴 삭제
//    public void removeOrderMenu(OrderMenu orderMenu) {
//        if (orderMenus.contains(orderMenu)) {
//            orderMenus.remove(orderMenu);
//        } else {
//            throw new IllegalArgumentException("Menu not found in the order.");
//        }
//    }
//    // 기존 메뉴를 삭제하는 메서드
//    public void clearOrderMenus() {
//        this.orderMenus.clear();  // 메뉴 리스트 초기화
//    }
//
//    // 새 메뉴 항목들을 추가하는 메서드
//    public void addOrderMenus(List<OrderMenu> orderMenus) {
//        this.orderMenus.addAll(orderMenus);  // 새 메뉴 항목들을 리스트에 추가
//    }
//
////    // 메뉴 추가 메서드
//    public void addOrderMenu(OrderMenu orderMenu) {
//        this.orderMenus.add(orderMenu);  // 단일 메뉴 항목 추가
//    }
    public void updateStatus(String status) {
        this.status = status;
    }

}
