package com.d205.KIWI_Backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Integer kioskId;
    private Integer age;  // 주문자의 나이
    private Integer gender;  // 주문자의 성별
    private Integer orderNumber;

    private List<MenuOrderRequest> menuOrders;  // 여러 메뉴에 대한 주문 정보

    @Getter
    @Setter
    @Builder
    public static class MenuOrderRequest {
        private Integer menuId;  // 메뉴 ID
        private Integer quantity;  // 주문 수량
    }
}
