package com.d205.KIWI_Backend.menu.dto;

import com.d205.KIWI_Backend.menu.domain.Menu;
import lombok.Data;

@Data
public class MenuViewResponse {

    private String name;
    private Integer price;
    private String description;
    private String imgPath;
    private Integer viewCount; // 조회 수 추가

    // Menu 객체와 조회 수를 받아서 MenuViewResponse 객체를 생성하는 메서드
    public static MenuViewResponse fromMenuWithViewCount(Menu menu, int viewCount) {
        MenuViewResponse response = new MenuViewResponse();
        response.setName(menu.getName());
        response.setPrice(menu.getPrice());
        response.setDescription(menu.getDescription());
        response.setImgPath(menu.getImgPath());
        response.setViewCount(viewCount); // 조회 수 설정
        return response;
    }
}