package com.d205.KIWI_Backend.menu.service;

import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_FOUND_MENU;

import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.global.exception.ExceptionCode;
import com.d205.KIWI_Backend.menu.domain.Menu;
import com.d205.KIWI_Backend.menu.domain.MenuCategory;
import com.d205.KIWI_Backend.menu.dto.MenuRequest;
import com.d205.KIWI_Backend.menu.dto.MenuResponse;
import com.d205.KIWI_Backend.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    // 메뉴 단건 등록
    @Transactional
    public MenuResponse createMenu(MenuRequest request) {
        Menu menu = Menu.builder()
            .category(request.getCategory())
            .categoryNumber(request.getCategoryNumber())
            .hotOrIce(request.getHotOrIce())
            .name(request.getName())
            .price(request.getPrice())
            .description(request.getDescription())
            .imgPath(request.getImgPath())
            .build();

        menu = menuRepository.save(menu);
        return MenuResponse.fromMenu(menu);
    }

    // 메뉴 전체 조회
    @Transactional(readOnly = true)
    public List<MenuResponse> getAllMenus() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::fromMenu)
            .collect(Collectors.toList());
    }

    // 메뉴 단건 조회
    @Transactional(readOnly = true)
    public MenuResponse getMenuById(Integer id) {
        return menuRepository.findById(id)
            .map(MenuResponse::fromMenu) // 변경된 부분
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_MENU));
    }

    // 메뉴 단건 수정
    @Transactional
    public MenuResponse updateMenu(Integer id, MenuRequest request) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() ->  new BadRequestException(NOT_FOUND_MENU));

        // 빌더 패턴을 사용하여 기존 메뉴 정보 수정
        Menu updatedMenu = Menu.builder()
            .id(menu.getId()) // 기존 ID를 유지
            .category(request.getCategory())
            .categoryNumber(request.getCategoryNumber())
            .hotOrIce(request.getHotOrIce())
            .name(request.getName())
            .price(request.getPrice())
            .description(request.getDescription())
            .imgPath(request.getImgPath())
            .build();

        updatedMenu = menuRepository.save(updatedMenu);
        return MenuResponse.fromMenu(updatedMenu); // 변경된 부분
    }

    // 메뉴 단건 삭제
    public void deleteMenu(Integer id) {
        if (!menuRepository.existsById(id)) {
            throw new BadRequestException(NOT_FOUND_MENU);
        }
        menuRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenusByCategory(String category) {
        if (!MenuCategory.isValidCategory(category)) {
            throw new BadRequestException(ExceptionCode.INVALID_CATEGORY);
        }

        return menuRepository.findByCategory(category).stream()
            .map(MenuResponse::fromMenu)
            .collect(Collectors.toList());
    }
}
