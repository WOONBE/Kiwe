package com.d205.KIWI_Backend.menu.domain;

import com.d205.KIWI_Backend.global.common.BaseEntity;
import com.d205.KIWI_Backend.order.domain.OrderMenu;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Menu extends BaseEntity {

    @Id
    @Column(name = "MENU_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "MENU_CATEGORY")
    private String category; // 카테고리

    @Column(name = "MENU_CATEGORY_NUM")
    private Integer categoryNumber; // 카테고리 번호

    @Column
    private String hotOrIce; // HOT/ICE

    @Column(name = "MENU_NAME")
    private String name; // 이름

    @Column(name = "MENU_PRICE")
    private Integer price; // 가격

    @Column(name = "MENU_DESC")
    private String description; // 설명

    @Column(name = "MENU_IMG")
    private String imgPath; // 이미지 경로

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<OrderMenu> orderMenus;

    // 연관관계 편의 메서드
    public void addOrderMenu(OrderMenu orderMenu) {
        this.orderMenus.add(orderMenu);
        if (orderMenu.getMenu() != this) {
            orderMenu.updateMenu(this);
        }
    }
}
