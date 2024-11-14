package com.d205.KIWI_Backend.order.controller;

import com.d205.KIWI_Backend.order.dto.OrderRequest;
import com.d205.KIWI_Backend.order.dto.OrderResponse;
import com.d205.KIWI_Backend.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_shouldReturnCreatedOrder() throws Exception {
        // OrderRequest 설정
        OrderRequest orderRequest = OrderRequest.builder()
            .kioskId(1)
            .age(25)
            .gender(1)
            .menuOrders(Arrays.asList(
                OrderRequest.MenuOrderRequest.builder().menuId(101).quantity(2).build(),
                OrderRequest.MenuOrderRequest.builder().menuId(102).quantity(1).build()
            ))
            .build();

        // OrderResponse 설정
        OrderResponse orderResponse = OrderResponse.builder()
            .orderId(1L)
            .orderDate(LocalDateTime.now())
            .status("PENDING")
            .totalPrice(20000)
            .kioskId(1)
            .menuOrders(Arrays.asList(
                OrderResponse.MenuOrderResponse.builder().menuId(101).name("Americano").quantity(2).price(5000).build(),
                OrderResponse.MenuOrderResponse.builder().menuId(102).name("Latte").quantity(1).price(10000).build()
            ))
            .build();

        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(orderResponse);

        // JSON content 생성
        String requestBody = "{ \"kioskId\": 1, \"age\": 25, \"gender\": 1, \"menuOrders\": [ { \"menuId\": 101, \"quantity\": 2 }, { \"menuId\": 102, \"quantity\": 1 } ] }";

        ResponseEntity<OrderResponse> result = orderController.createOrder(orderRequest);
        // 응답 상태 및 내용 검증
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getOrderId());
        assertEquals("PENDING", result.getBody().getStatus());
        assertEquals(20000, result.getBody().getTotalPrice());

        verify(orderService, times(1)).createOrder(any(OrderRequest.class));
    }

    @Test
    void getOrderById_shouldReturnOrder() throws Exception {
        OrderResponse orderResponse = OrderResponse.builder()
            .orderId(1L)
            .orderDate(LocalDateTime.now())
            .status("COMPLETED")
            .totalPrice(15000)
            .kioskId(1)
            .menuOrders(Collections.singletonList(
                OrderResponse.MenuOrderResponse.builder().menuId(101).name("Cappuccino").quantity(1).price(15000).build()
            ))
            .build();

        when(orderService.getOrderById(anyLong())).thenReturn(orderResponse);


        ResponseEntity<OrderResponse> result = orderController.getOrderById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getOrderId());
        assertEquals("COMPLETED", result.getBody().getStatus());
        assertEquals(15000, result.getBody().getTotalPrice());
        verify(orderService, times(1)).getOrderById(anyLong());
    }



    @Test
    void getAllOrders_shouldReturnAllOrders() throws Exception {
        OrderResponse orderResponse1 = OrderResponse.builder()
            .orderId(1L)
            .orderDate(LocalDateTime.now())
            .status("COMPLETED")
            .totalPrice(15000)
            .kioskId(1)
            .menuOrders(Collections.singletonList(
                OrderResponse.MenuOrderResponse.builder().menuId(101).name("Latte").quantity(1).price(15000).build()
            ))
            .build();

        OrderResponse orderResponse2 = OrderResponse.builder()
            .orderId(2L)
            .orderDate(LocalDateTime.now())
            .status("PENDING")
            .totalPrice(30000)
            .kioskId(2)
            .menuOrders(Collections.singletonList(
                OrderResponse.MenuOrderResponse.builder().menuId(102).name("Mocha").quantity(3).price(10000).build()
            ))
            .build();

        List<OrderResponse> orderResponses = Arrays.asList(orderResponse1, orderResponse2);

        when(orderService.getAllOrders()).thenReturn(orderResponses);

        ResponseEntity<List<OrderResponse>> result = orderController.getAllOrders();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(orderResponses.size(), result.getBody().size());

        verify(orderService, times(1)).getAllOrders();
    }
}
