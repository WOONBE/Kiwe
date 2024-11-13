package com.d205.KIWI_Backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuSales {
        private Integer menuId;
        private String menuName;
        private Integer sales;



}