package com.d205.KIWI_Backend.order.dto;// package com.d205.KIWI_Backend.kafka.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderEventDto {

    private int kioskId;
    private LocalDateTime orderDate;
    private CustomerInfoDto customerInfo;
    private List<OrderItemDto> orderItems;

    @Data
    @Builder
    public static class CustomerInfoDto {
        private int age;
        private int gender;
    }

    @Data
    @Builder
    public static class OrderItemDto {
        private int menuId;
        private int quantity;
    }
}