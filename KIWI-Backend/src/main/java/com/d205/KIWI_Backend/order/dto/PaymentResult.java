package com.d205.KIWI_Backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class PaymentResult {
    private String menuCategory;
    private String menuName;
    private int menuPrice;
    private int quantity;
    private int totalPrice;

    // Native Query 결과와 매핑될 생성자
    public PaymentResult(String menuCategory, String menuName, int menuPrice, int quantity, int totalPrice) {
        this.menuCategory = menuCategory;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
