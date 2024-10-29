package com.d205.KIWI_Backend.menu.dto;

import com.d205.KIWI_Backend.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {
    private Integer id;
    private String category;
    private Integer categoryNumber;
    private String hotOrIce;
    private String name;
    private Integer price;
    private String description;
    private String imgPath;

    public static MenuResponse fromMenu(Menu menu) {
        return MenuResponse.builder()
            .id(menu.getId())
            .category(menu.getCategory())
            .categoryNumber(menu.getCategoryNumber())
            .hotOrIce(menu.getHotOrIce())
            .name(menu.getName())
            .price(menu.getPrice())
            .description(menu.getDescription())
            .imgPath(menu.getImgPath())
            .build();
    }
}
