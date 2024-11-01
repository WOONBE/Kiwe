package com.d205.KIWI_Backend.menu.service;

import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_FOUND_MENU;

import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.global.exception.ExceptionCode;
import com.d205.KIWI_Backend.log.domain.ViewCount;
import com.d205.KIWI_Backend.log.service.MenuStatisticsService;
import com.d205.KIWI_Backend.menu.domain.Menu;
import com.d205.KIWI_Backend.menu.domain.MenuCategory;
import com.d205.KIWI_Backend.menu.dto.MenuRequest;
import com.d205.KIWI_Backend.menu.dto.MenuResponse;
import com.d205.KIWI_Backend.menu.dto.MenuViewResponse;
import com.d205.KIWI_Backend.menu.repository.MenuRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuStatisticsService menuStatisticsService;
    private final Logger logger = LoggerFactory.getLogger(MenuService.class);

    private final ObjectMapper objectMapper;

    @Value("${server.image.base-url}")
    private String defaultUrl;

    // 메뉴 단건 등록
//    @Transactional
//    @CacheEvict(cacheNames = "menusByCategory", key = "#request.category", cacheManager = "rcm")
//    public MenuResponse createMenu(MenuRequest request) {
//        Menu menu = Menu.builder()
//            .category(request.getCategory())
//            .categoryNumber(request.getCategoryNumber())
//            .hotOrIce(request.getHotOrIce())
//            .name(request.getName())
//            .price(request.getPrice())
//            .description(request.getDescription())
//            .imgPath(request.getImgPath())
//            .build();
//
//        menu = menuRepository.save(menu);
//        return MenuResponse.fromMenu(menu);
//    }

    @Transactional
//    @CachePut(cacheNames = "menusByCategory", key = "#request.category", cacheManager = "rcm")
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
        // 새로 저장된 데이터를 즉시 캐싱
        getMenusByCategory(request.getCategory());
        return MenuResponse.fromMenu(menu);
    }

    // 메뉴 전체 조회
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "menus", key = "'allMenus'", sync = true, cacheManager = "rcm")
    public List<MenuResponse> getAllMenus() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::fromMenu)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
//    @Cacheable(cacheNames = "menu_one", key = "#id", sync = true, cacheManager = "rcm")
    public MenuResponse getMenuById(Integer id) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_MENU));

        // 로그 기록
        logger.info("메뉴 조회: ID={}, 이름={}", menu.getId(), menu.getName());

        // MenuResponse로 변환
        return MenuResponse.fromMenu(menu); // 수정된 부분
    }




    // 메뉴 단건 수정
    @Transactional
//    @CachePut(cacheNames = "menusByCategory", key = "#request.category", cacheManager = "rcm")
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

//    // 메뉴 단건 삭제
//    @Transactional
//    @CacheEvict(cacheNames = {"menus", "menusByCategory"}, key = "#category", cacheManager = "rcm")
//    public void deleteMenu(Integer id) {
//        if (!menuRepository.existsById(id)) {
//            throw new BadRequestException(NOT_FOUND_MENU);
//        }
//        menuRepository.deleteById(id);
//    }

    @Transactional
    public void deleteMenu(Integer id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new BadRequestException(NOT_FOUND_MENU));
        menuRepository.deleteById(id);
        // 메뉴가 삭제된 후 캐시 무효화 및 갱신
        getMenusByCategory(menu.getCategory());
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "menusByCategory", key = "#category", sync = true, cacheManager = "rcm")
    public List<MenuResponse> getMenusByCategory(String category) {
        if (!MenuCategory.isValidCategory(category)) {
            throw new BadRequestException(ExceptionCode.INVALID_CATEGORY);
        }

        return menuRepository.findByCategory(category).stream()
            .map(MenuResponse::fromMenu)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuViewResponse> getAllMenusWithViewCounts() { // 수정된 부분
        List<Menu> menus = menuRepository.findAll();

        // MenuStatisticsService에서 조회 수를 가져옴
        List<ViewCount> viewCounts = menuStatisticsService.getMenuViewCounts();

        // ViewCounts를 요청 URI에 따라 맵핑하여 접근 용이하게 함
        Map<String, Integer> viewCountMap = viewCounts.stream()
            .collect(Collectors.toMap(ViewCount::getRequestURI, ViewCount::getViewCount));

        return menus.stream()
            .map(menu -> {
                int viewCount = viewCountMap.getOrDefault(defaultUrl+menu.getImgPath(), 0);
                return MenuViewResponse.fromMenuWithViewCount(menu, viewCount); // 수정된 부분
            })
            .collect(Collectors.toList());
    }


}
