package com.d205.KIWI_Backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;  // 주문 ID
    private LocalDateTime orderDate;  // 주문 날짜
    private String status;  // 주문 상태
    private List<MenuOrderResponse> menuOrders;  // 주문한 메뉴들
    private Integer totalPrice;  // 총 가격
    private Integer kioskId;  // 키오스크 ID 추가
    private Integer orderNumber;

    @Getter
    @Setter
    @Builder
    public static class MenuOrderResponse {
        private Integer menuId;  // 메뉴 ID
        private String name;  // 메뉴 이름
        private Integer quantity;  // 수량
        private Integer price;  // 가격
        private Integer orderNumber; //번호표
    }
}
