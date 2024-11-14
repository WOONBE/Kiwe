package com.d205.KIWI_Backend.menu.controller;

import com.d205.KIWI_Backend.menu.dto.MenuRequest;
import com.d205.KIWI_Backend.menu.dto.MenuResponse;
import com.d205.KIWI_Backend.menu.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class MenuControllerTest {


    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("메뉴 단건 생성")
    void createMenu() throws Exception {

        MenuRequest request = new MenuRequest("Drink", "Hot", "Americano", 3000, "Delicious coffee",
            "/images/americano.jpg");
        MenuResponse response = new MenuResponse(1, "Drink", 1, "Hot", "Americano", 3000,
            "Delicious coffee", "/images/americano.jpg");

        given(menuService.createMenu(any(MenuRequest.class))).willReturn(response);

        ResponseEntity<MenuResponse> result = menuController.createMenu(request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(menuService).createMenu(any(MenuRequest.class));


    }

    @Test
    @DisplayName("메뉴 전체 조회")
    void getAllMenus() throws Exception {
        MenuResponse menu1 = new MenuResponse(1, "Drink", 1, "Hot", "Americano", 3000, "Delicious coffee", "/images/americano.jpg");
        MenuResponse menu2 = new MenuResponse(2, "Food", 1, "N/A", "Sandwich", 5000, "Tasty sandwich", "/images/sandwich.jpg");

        List<MenuResponse> menuList = Arrays.asList(menu1, menu2);

        given(menuService.getAllMenus()).willReturn(menuList);
        ResponseEntity<List<MenuResponse>> result = menuController.getAllMenus();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(menuService).getAllMenus();


    }
//
    @Test
    @DisplayName("메뉴 단건 조회")
    void getMenuById() throws Exception {
        MenuResponse response = new MenuResponse(1, "Drink", 1, "Hot", "Americano", 3000, "Delicious coffee", "/images/americano.jpg");
        given(menuService.getMenuById(anyInt())).willReturn(response);

        ResponseEntity<MenuResponse> result = menuController.getMenuById(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(menuService).getMenuById(anyInt());



    }

    @Test
    @DisplayName("메뉴 단건 수정")
    void updateMenu() throws Exception {
        MenuRequest request = new MenuRequest("Drink", "Hot", "Americano", 3200, "Updated coffee description", "/images/americano.jpg");
        MenuResponse response = new MenuResponse(1, "Drink", 1, "Hot", "Americano", 3200, "Updated coffee description", "/images/americano.jpg");

        given(menuService.updateMenu(anyInt(), any(MenuRequest.class))).willReturn(response);
        ResponseEntity<MenuResponse> result = menuController.updateMenu(1, request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(menuService).updateMenu(anyInt(), any(MenuRequest.class));

    }


}
