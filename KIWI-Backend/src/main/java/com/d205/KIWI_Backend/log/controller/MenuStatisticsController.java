package com.d205.KIWI_Backend.log.controller;

import com.d205.KIWI_Backend.log.domain.ViewCount;
import com.d205.KIWI_Backend.log.service.MenuStatisticsService;
import com.d205.KIWI_Backend.menu.dto.MenuResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuStatisticsController {

    private final MenuStatisticsService menuStatisticsService;

    @GetMapping("/api/popular-menus")
    @Operation(summary = "최다 판매 메뉴 조회", description = "일주일간 가장 많이 팔린 메뉴들 10개 조회")
    public List<ViewCount> getPopularMenus() {
        return menuStatisticsService.getTop10Menus();
    }

    @GetMapping("/api/suggested")
    @Operation(summary = "메뉴 추천", description = "최다 판매 기준으로 메뉴를 추천")
    public ResponseEntity<List<MenuResponse>> getSuggestedMenus() {
        List<MenuResponse> suggestedMenus = menuStatisticsService.suggestedMenus();
        return ResponseEntity.ok(suggestedMenus);
    }
}
