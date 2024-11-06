package com.d205.KIWI_Backend.order.controller;

import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.order.dto.OrderRequest;
import com.d205.KIWI_Backend.order.dto.OrderResponse;
import com.d205.KIWI_Backend.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "주문을 생성하는 API")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    // 단건 조회 API
    @GetMapping("/{orderId}")
    @Operation(summary = "주문 단건 조회", description = "주문을 단건 조회하는 API")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    // 전체 조회 API
    @GetMapping("/all")
    @Operation(summary = "주문 전체 조회", description = "주문을 전체 조회하는 API")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orderResponses = orderService.getAllOrders();
        return ResponseEntity.ok(orderResponses);
    }
//
//    @PutMapping("/{orderId}")
//    @Operation(summary = "주문 업데이트", description = "주문을 업데이트하는 API")
//    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long orderId, @RequestBody OrderRequest orderRequest) {
//        OrderResponse orderResponse = orderService.updateOrder(orderId, orderRequest);
//        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
//    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 삭제", description = "주문을 삭제하는 API")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("payment/{kioskId}")
    @Operation(summary = "주문 상태 확인", description = "주문의 진행 상태를 반환하는 API")
    public ResponseEntity<String> getOrderStatus(@PathVariable Long kioskId) {
        String status = orderService.getOrderStatus(kioskId);
        return ResponseEntity.ok(status);
    }

    @PutMapping("payment/{kioskId}")
    @Operation(summary = "주문 결제 처리", description = "키오스크의 마지막 주문을 결제 처리하는 API")
    public ResponseEntity<String> updateOrderStatusToCompleted(@PathVariable Long kioskId) {
        try {
            String status = orderService.updateOrderStatusToCompleted(kioskId);
            return ResponseEntity.ok(status);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}