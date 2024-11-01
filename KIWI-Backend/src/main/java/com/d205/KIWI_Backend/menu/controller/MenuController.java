package com.d205.KIWI_Backend.menu.controller;

import com.d205.KIWI_Backend.menu.dto.MenuRequest;
import com.d205.KIWI_Backend.menu.dto.MenuResponse;
import com.d205.KIWI_Backend.menu.dto.MenuViewResponse;
import com.d205.KIWI_Backend.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 메뉴 단건 등록
    @PostMapping
    @Operation(summary = "메뉴 생성", description = "메뉴를 단건으로 생성하는 API")
    public ResponseEntity<MenuResponse> createMenu(@RequestBody MenuRequest request) {
        MenuResponse response = menuService.createMenu(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 메뉴 전체 조회
    @GetMapping("/all")
    @Operation(summary = "메뉴 전체 조회", description = "메뉴를 전체 조회하는 API")
    public ResponseEntity<List<MenuResponse>> getAllMenus() {
        List<MenuResponse> responses = menuService.getAllMenus();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 메뉴 단건 조회
    @GetMapping("/{id}")
    @Operation(summary = "메뉴 단건 조회", description = "메뉴를 단건 조회하는 API")
    public ResponseEntity<MenuResponse> getMenuById(@PathVariable Integer id) {
        MenuResponse response = menuService.getMenuById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 메뉴 단건 수정
    @PutMapping("/{id}")
    @Operation(summary = "메뉴 수정", description = "메뉴를 수정하는 API")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Integer id, @RequestBody MenuRequest request) {
        MenuResponse response = menuService.updateMenu(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 메뉴 단건 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "메뉴 삭제", description = "메뉴를 삭제하는 API")
    public ResponseEntity<Void> deleteMenu(@PathVariable Integer id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // 카테고리별 메뉴 조회
    @GetMapping("/category")
    @Operation(summary = "카테고리 조회", description = "카테고리별 메뉴를 조회하는 API")
    public ResponseEntity<List<MenuResponse>> getMenusByCategory(@RequestParam String category) {
        List<MenuResponse> responses = menuService.getMenusByCategory(category);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/view-counts") // 해당 메서드에 대한 GET 요청 경로
    public ResponseEntity<List<MenuViewResponse>> getAllMenusWithViewCounts() {
        List<MenuViewResponse> menuViewResponses = menuService.getAllMenusWithViewCounts();
        return ResponseEntity.ok(menuViewResponses); // 200 OK 응답 반환
    }
}
