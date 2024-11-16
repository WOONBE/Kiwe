package com.d205.KIWI_Backend.order.controller;

import com.d205.KIWI_Backend.member.service.MemberService;
import com.d205.KIWI_Backend.order.dto.OrderResponse;
import com.d205.KIWI_Backend.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/view/orders")
public class OrderViewController {
    private final OrderService orderService;

    @Autowired
    public OrderViewController(OrderService orderService) {
        this.orderService = orderService;
    }

    
    @GetMapping("/latest/{kioskId}")
    @Operation(summary = "키오스크 최신 주문 영수증 출력", description = "키오스크의 주문에 대한 영수증 출력")
    public String getLatestOrderView(@PathVariable Long kioskId, Model model) {
        List<OrderResponse.MenuOrderResponse> orderMenus = orderService.getLatestOrderMenuByKioskId(kioskId);

        // 주문 데이터에 총 가격(totalPrice) 계산 추가 (옵션)
        List<Map<String, Object>> enrichedOrders = orderMenus.stream()
                .map(order -> {
                    Map<String, Object> enrichedOrder = new HashMap<>();
                    enrichedOrder.put("menuName", order.getName());
                    enrichedOrder.put("menuPrice", order.getPrice());
                    enrichedOrder.put("quantity", order.getQuantity());
                    enrichedOrder.put("totalPrice", order.getPrice() * order.getQuantity());
                    return enrichedOrder;
                })
                .toList();

        model.addAttribute("orderMenus", enrichedOrders);
        return "order-list"; // order-list.html을 렌더링
        //TODO : orders 테이블에 주문번호 전달 - html에 주문번호 삽입 필요
    }
}
