package com.d205.KIWI_Backend.menu.domain;

import com.d205.KIWI_Backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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










}
