package com.d205.KIWI_Backend.order.controller;

import com.d205.KIWI_Backend.order.dto.OrderResponse;
import com.d205.KIWI_Backend.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/view/orders")
public class OrderViewController {
    private final OrderService orderService;

    @Autowired
    public OrderViewController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/latest/{kioskId}")
    public String getLatestOrderView(@PathVariable Long kioskId, Model model) {
        List<OrderResponse.MenuOrderResponse> orderMenus = orderService.getLatestOrderMenuByKioskId(kioskId);

        DecimalFormat formatter = new DecimalFormat("###,###"); // 세자리마다 쉼표 추가

        // 주문 데이터에 총 가격(totalPrice) 계산 추가
        List<Map<String, Object>> enrichedOrders = orderMenus.stream()
                .map(order -> {
                    Map<String, Object> enrichedOrder = new HashMap<>();
                    enrichedOrder.put("menuName", order.getName());
                    enrichedOrder.put("menuPrice", order.getPrice()); // 가격을 숫자로 그대로 저장
                    enrichedOrder.put("quantity", order.getQuantity());
                    enrichedOrder.put("totalPrice", order.getPrice() * order.getQuantity()); // 계산은 숫자 그대로 처리
                    return enrichedOrder;
                })
                .collect(Collectors.toList());

        // 포맷된 가격을 계산할 때만 포맷 적용
        enrichedOrders.forEach(order -> {
            order.put("formattedMenuPrice", formatter.format(order.get("menuPrice")));
            order.put("formattedTotalPrice", formatter.format(order.get("totalPrice")));
        });

        long totalOrderPrice = enrichedOrders.stream()
                .mapToLong(order -> ((Number) order.get("totalPrice")).longValue())
                .sum();

        model.addAttribute("orderMenus", enrichedOrders);
        model.addAttribute("totalOrderPrice", formatter.format(totalOrderPrice)); // 포맷된 총 주문 금액

        return "order-list";
    }

}
