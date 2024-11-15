package com.d205.KIWI_Backend.order.controller;

import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.member.service.MemberService;
import com.d205.KIWI_Backend.menu.dto.MenuResponse;
import com.d205.KIWI_Backend.order.dto.MenuSales;
import com.d205.KIWI_Backend.order.dto.OrderRequest;
import com.d205.KIWI_Backend.order.dto.OrderResponse;
import com.d205.KIWI_Backend.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import java.time.YearMonth;
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
    private final MemberService memberService;

    @Autowired
    public OrderController(OrderService orderService, MemberService memberService) {
        this.orderService = orderService;
        this.memberService = memberService;
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

    @GetMapping("/kiosk/{kioskId}")
    @Operation(summary = "특정 키오스크의 주문 전체 조회", description = "특정 키오스크의 주문을 전체 조회하는 API")
    public ResponseEntity<List<OrderResponse>> getOrdersByKioskId(@PathVariable Integer kioskId) {
        List<OrderResponse> orderResponses = orderService.getOrdersByKioskId(kioskId);
        if (orderResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/kiosk/top-selling-menus")
    @Operation(summary = "키오스크별 연령대별 인기 메뉴 조회", description = "특정 키오스크에서 연령대별 인기 메뉴를 조회하는 API")
    public Map<String, Map<String, Integer>> getTopSellingMenusByAgeGroupByKioskId(
        @RequestParam Integer kioskId) {
        return orderService.getTopSellingMenusByAgeGroupByKioskId(kioskId);
    }

    @GetMapping("/all/top-selling-menus")
    @Operation(summary = "전체 주문 기반 연령대별 인기 메뉴 조회", description = "전체 주문을 기반으로 연령대별 인기 메뉴를 조회하는 API")
    public Map<String, Map<String, Integer>> getTopSellingMenusByAgeGroup() {

        return orderService.getTopSellingMenusByAgeGroup();
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

    @GetMapping("/payment/{kioskId}")
    @Operation(summary = "주문 상태 확인", description = "주문의 진행 상태를 반환하는 API")
    public ResponseEntity<String> getOrderStatus(@PathVariable Long kioskId) {
        String status = orderService.getOrderStatus(kioskId);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/payment/{kioskId}")
    @Operation(summary = "주문 결제 처리", description = "키오스크의 마지막 주문을 결제 처리하는 API")
    public ResponseEntity<String> updateOrderStatusToCompleted(@PathVariable Long kioskId) {
        try {
            String status = orderService.updateOrderStatusToCompleted(kioskId);
            return ResponseEntity.ok(status);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/payment/{kioskId}")
    @Operation(summary = "키오스크 최근 주문 삭제", description = "키오스크의 최근 주문을 삭제하는 API")
    public ResponseEntity<Void> cancelKioskOrder(@PathVariable Long kioskId) {
        try {
            orderService.cancelKioskOrder(kioskId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/total-price/last-month")
    @Operation(summary = "한달 간 총 주문금액", description = "한달 간 총 주문금액을 리턴하는 API")
    public ResponseEntity<Integer> getTotalPriceForLastMonth() {
        int totalPrice = orderService.calculateTotalPriceForLastMonth(); // 서비스 호출하여 총 금액 계산
        return ResponseEntity.ok(totalPrice); // 계산된 총 금액을 ResponseEntity로 반환
    }

    @GetMapping("/total-price/last-month/{kioskId}")
    @Operation(summary = "특정 키오스크의 한달 간 총 주문금액", description = "특정 키오스크의 한달 간 총 주문금액을 리턴하는 API")
    public ResponseEntity<Integer> getTotalPriceForLastMonthByKioskId(@PathVariable Integer kioskId) {
        int totalPrice = orderService.calculateTotalPriceForLastMonthByKioskId(kioskId); // 키오스크 ID 기준으로 총 금액 계산
        return ResponseEntity.ok(totalPrice); // 결과 반환
    }

    // 특정 키오스크 ID 기준으로 최근 3개월간 월별 매출 조회
    @GetMapping("/monthly-sales/last-six-months/{kioskId}")
    @Operation(summary = "특정 키오스크의 6개월 간 주문금액", description = "특정 키오스크의 6개월 간 주문금액을 월별로 리턴하는 API")
    public ResponseEntity<Map<YearMonth, Integer>> getMonthlySalesForLastThreeMonthsByKioskId(@PathVariable Integer kioskId) {
        Map<YearMonth, Integer> monthlySales = orderService.calculateMonthlyTotalForLastSixMonthsByKioskId(kioskId);
        return ResponseEntity.ok(monthlySales);
    }

    @GetMapping("/top-sold-menus/{memberId}")
    @Operation(summary = "특정 멤버가 운영하는 키오스크의 연령대별 인기 메뉴 조회", description = "특정 멤버가 운영하는 키오스크의 연령대별 인기 메뉴 조회하는 API")
    public Map<String, List<MenuSales>> getTopSoldMenusByMemberId(@PathVariable Integer memberId) {
        return orderService.getTopSoldMenusByMemberId(memberId);
    }

    @GetMapping("/top-sold-menus")
    @Operation(summary = "로그인 된 멤버가 운영하는 키오스크의 연령대별 인기 메뉴 조회", description = "로그인 된 멤버가 운영하는 키오스크의 연령대별 인기 메뉴 조회하는 API")
    public Map<String, List<MenuSales>> getTopSoldMenusByLoginMember() {
        return orderService.getTopSoldMenusByLoginMember();
    }

    @GetMapping("/total-price/last-month/member")
    @Operation(summary = "로그인 된 멤버의 모든 키오스크의 한달 간 총 수익", description = "특정 멤버가 운영하는 모든 키오스크의 한달 간 총 수익을 리턴하는 API")
    public ResponseEntity<Integer> getTotalPriceForLastMonthByMemberId() {
        int totalPrice = orderService.calculateTotalPriceForLastMonthByMemberId();
        return ResponseEntity.ok(totalPrice);
    }


    @GetMapping("/order-count-last-month")
    @Operation(summary = "로그인 된 멤버가 운영하는 키오스크의 한달 주문 횟수", description = "로그인 된 멤버가 운영하는 키오스크의 한달 주문 횟수을 리턴하는 API")
    public ResponseEntity<Integer> getOrderCountForLastMonth() {
        int orderCount = orderService.calculateOrderCountForLastMonthByMemberId();

        return ResponseEntity.ok(orderCount);
    }

    @GetMapping("/order-count/last-six-months")
    @Operation(summary = "로그인 된 멤버가 운영하는 키오스크의 6개월 별 주문 횟수", description = "특정 멤버가 운영하는 키오스크의 지난 6개월 동안의 주문 횟수를 월별로 리턴하는 API")
    public ResponseEntity<Map<YearMonth, Integer>> getMonthlyOrderCount() {
        Integer memberId = memberService.getCurrentMemberId();
        Map<YearMonth, Integer> monthlyCounts = orderService.calculateOrderCountForLastSixMonthsByMemberId(memberId);
        return ResponseEntity.ok(monthlyCounts);
    }



    @GetMapping("/top-20-menu-sales")
    @Operation(summary = "로그인 된 멤버가 운영하는 키오스크의 판매량 별 메뉴 조회", description = "로그인 된 멤버가 운영하는 키오스크의 판매량 별 메뉴 조회하는 API")
    public ResponseEntity<List<MenuResponse>> getTop20MenuSales() {
        // 서비스 메서드를 호출하여 판매 수량이 많은 상위 20개의 메뉴 가져오기
        List<MenuResponse> top20MenuSales = orderService.getTop20MenuSalesForLastMonthByMemberId();

        return ResponseEntity.ok(top20MenuSales);
    }

    @GetMapping("/monthly-sales/last-six-months/member")
    @Operation(summary = "멤버의 키오스크 월별 매출 조회", description = "특정 멤버가 운영하는 키오스크의 지난 6개월 동안의 매출을 월별로 리턴하는 API")
    public ResponseEntity<Map<YearMonth, Integer>> getMonthlySalesForLastSixMonthsByMemberId() {
        Integer memberId = memberService.getCurrentMemberId();
        Map<YearMonth, Integer> monthlySales = orderService.getMonthlySalesForLastSixMonthsByMemberId(memberId);
        return ResponseEntity.ok(monthlySales);
    }










}
