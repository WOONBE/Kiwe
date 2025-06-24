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

    @Column
    private Integer age;

    @Column
    private Integer gender;

    @Column
    private Integer orderNumber;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderMenu> orderMenus = new ArrayList<>();

    //kafka 사용시 삭제해도 무방
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<KioskOrder> kioskOrders = new ArrayList<>(); // 주문과 관련된 키오스크 목록

    // 추가된 필드: 어떤 키오스크에서 발생한 주문인지 명시
    @Column(name = "kiosk_id", nullable = false)
    private Integer kioskId;

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

    public void updateStatus(String status) {
        this.status = status;
    }

    public void addKioskOrder(KioskOrder kioskOrder) {
        this.kioskOrders.add(kioskOrder);
        if (kioskOrder.getOrder() != this) {
            kioskOrder.updateOrder(this);
        }
    }

    public Integer getKioskId() {
        if (kioskOrders != null && !kioskOrders.isEmpty()) {
            return kioskOrders.get(0).getKiosk().getId();  // 첫 번째 KioskOrder에서 KioskId 추출
        }
        return null;
    }

    public void updateAge(Integer age) {
        this.age = age;
    }

    public void updateGender(Integer gender) {
        if (gender == 1 || gender == 2) {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Gender must be 1 or 2");
        }
    }

}



















