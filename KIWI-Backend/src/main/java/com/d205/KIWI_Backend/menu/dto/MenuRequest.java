package com.d205.KIWI_Backend.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequest {
    private String category;
    private Integer categoryNumber;
    private String hotOrIce;
    private String name;
    private Integer price;
    private String description;
    private String imgPath;
}
