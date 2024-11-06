package com.d205.KIWI_Backend.order.service;

import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_FOUND_KIOSK_ID;
import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_FOUND_ORDER;

import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.kiosk.repository.KioskRepository;
import com.d205.KIWI_Backend.menu.domain.Menu;
import com.d205.KIWI_Backend.menu.repository.MenuRepository;
import com.d205.KIWI_Backend.menu.service.MenuService;
import com.d205.KIWI_Backend.order.domain.KioskOrder;
import com.d205.KIWI_Backend.order.domain.Order;
import com.d205.KIWI_Backend.order.domain.OrderMenu;
import com.d205.KIWI_Backend.order.dto.OrderRequest;
import com.d205.KIWI_Backend.order.dto.OrderResponse;
import com.d205.KIWI_Backend.order.repository.OrderRepository;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final KioskRepository kioskRepository;
    private final Logger logger = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, KioskRepository kioskRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.kioskRepository = kioskRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {


        // 주문 객체 생성 (빌더 패턴 사용)
        Order order = Order.builder()
            .orderDate(LocalDateTime.now())
            .status("PENDING")  // 기본 상태: PENDING
            .build();

        int totalPrice = 0;
        List<OrderMenu> orderMenus = new ArrayList<>();

        // 메뉴 주문 항목 처리
        for (OrderRequest.MenuOrderRequest menuOrderRequest : orderRequest.getMenuOrders()) {
            Optional<Menu> menuOptional = menuRepository.findById(menuOrderRequest.getMenuId());
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();

                logger.info("menu_view_event: ID={}, Name={}, Category={}", menu.getId(), menu.getName(), menu.getCategory());
                OrderMenu orderMenu = OrderMenu.builder()
                    .menu(menu)
                    .quantity(menuOrderRequest.getQuantity())
                    .build();

                order.addOrderMenu(orderMenu);  // 주문에 메뉴 추가
                orderMenus.add(orderMenu);

                totalPrice += menu.getPrice() * menuOrderRequest.getQuantity();
            }
        }

        // 키오스크 ID 설정
        Optional<Kiosk> kioskOptional = kioskRepository.findById(orderRequest.getKioskId());
        if (kioskOptional.isPresent()) {
            Kiosk kiosk = kioskOptional.get();

            // KioskOrder 객체 생성 후 주문에 연결
            KioskOrder kioskOrder = KioskOrder.builder()
                .kiosk(kiosk)  // 키오스크 연결
                .order(order)   // 주문 연결
                .assignedTime(LocalDateTime.now())  // 주문이 키오스크에 배정된 시간
                .build();

            order.addKioskOrder(kioskOrder);  // 주문에 KioskOrder 추가
        }

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // Response 생성 (빌더 패턴 사용)
        OrderResponse orderResponse = OrderResponse.builder()
            .orderId(savedOrder.getId())
            .orderDate(savedOrder.getOrderDate())
            .status(savedOrder.getStatus())
            .menuOrders(createMenuOrderResponses(orderMenus))  // 주문 메뉴 항목 응답 리스트 생성
            .totalPrice(totalPrice)
            .kioskId(orderRequest.getKioskId())  // 요청받은 키오스크 ID
            .build();

        return orderResponse;
    }


    @Transactional
    public OrderResponse getOrderById(Long orderId) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        Order order = existingOrder.get();
        List<OrderMenu> orderMenus = order.getOrderMenus(); // 기존 메뉴 항목

        int totalPrice = orderMenus.stream()
            .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
            .sum();

        // Response 생성
        return OrderResponse.builder()
            .orderId(order.getId())
            .orderDate(order.getOrderDate())
            .status(order.getStatus())
            .menuOrders(createMenuOrderResponses(orderMenus))
            .kioskId(order.getKioskOrders().get(0).getKiosk().getId())  // 첫 번째 키오스크 정보 가져오기
            .totalPrice(totalPrice)
            .build();
    }

    // 전체 조회
    @Transactional
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderMenu> orderMenus = order.getOrderMenus(); // 주문에 포함된 메뉴

            int totalPrice = orderMenus.stream()
                .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
                .sum();


            OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .menuOrders(createMenuOrderResponses(orderMenus))
                .kioskId(order.getKioskOrders().get(0).getKiosk().getId())  // 첫 번째 키오스크 정보 가져오기
                .totalPrice(totalPrice)
                .build();

            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        // 주문 조회
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        Order order = existingOrder.get();
        order.updateStatus("UPDATED");  // 주문 상태를 'UPDATED'로 변경 (상태 변경 예시)

        List<OrderMenu> orderMenus = new ArrayList<>();
        for (OrderRequest.MenuOrderRequest menuOrderRequest : orderRequest.getMenuOrders()) {
            Optional<Menu> menuOptional = menuRepository.findById(menuOrderRequest.getMenuId());
            if (menuOptional.isPresent()) {
                Menu menu = menuOptional.get();
                OrderMenu orderMenu = OrderMenu.builder()
                    .menu(menu)
                    .quantity(menuOrderRequest.getQuantity())
                    .build();
                order.addOrderMenu(orderMenu);
                orderMenus.add(orderMenu);
            }
        }

        // 기존 메뉴를 새로 업데이트된 메뉴로 교체
        order.clearOrderMenus(); // 기존 주문 메뉴를 삭제
        order.addOrderMenus(orderMenus); // 새로운 주문 메뉴 추가

        Order updatedOrder = orderRepository.save(order);

        // 총 가격 계산
        int totalPrice = orderMenus.stream()
            .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
            .sum();

        // Response 생성 (빌더 패턴 사용)
        OrderResponse orderResponse = OrderResponse.builder()
            .orderId(updatedOrder.getId())
            .orderDate(updatedOrder.getOrderDate())
            .status(updatedOrder.getStatus())
            .menuOrders(createMenuOrderResponses(orderMenus))  // totalPrice 포함
            .totalPrice(totalPrice)
            .build();

        return orderResponse;
    }
    @Transactional
    public void deleteOrder(Long orderId) {
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        orderRepository.delete(existingOrder.get());  // 주문 삭제
    }

//    // 메뉴 주문 응답 리스트를 생성하는 메서드 (공통 메서드로 분리)
//    private List<OrderResponse.MenuOrderResponse> createMenuOrderResponses(List<OrderMenu> orderMenus, int totalPrice) {
//        List<OrderResponse.MenuOrderResponse> menuOrderResponses = new ArrayList<>();
//        for (OrderMenu orderMenu : orderMenus) {
//            OrderResponse.MenuOrderResponse menuOrderResponse = OrderResponse.MenuOrderResponse.builder()
//                .menuId(orderMenu.getMenu().getId())
//                .name(orderMenu.getMenu().getName())
//                .quantity(orderMenu.getQuantity())
//                .price(orderMenu.getMenu().getPrice())
//                .build();
//            menuOrderResponses.add(menuOrderResponse);
//        }
//        return menuOrderResponses;
//    }
    private List<OrderResponse.MenuOrderResponse> createMenuOrderResponses(List<OrderMenu> orderMenus) {
        List<OrderResponse.MenuOrderResponse> menuOrderResponses = new ArrayList<>();
        for (OrderMenu orderMenu : orderMenus) {
            OrderResponse.MenuOrderResponse menuOrderResponse = OrderResponse.MenuOrderResponse.builder()
                .menuId(orderMenu.getMenu().getId())
                .name(orderMenu.getMenu().getName())
                .quantity(orderMenu.getQuantity())
                .price(orderMenu.getMenu().getPrice())
                .build();
            menuOrderResponses.add(menuOrderResponse);
        }
        return menuOrderResponses;
    }
//    private Integer getNextOrderNumberForKiosk(Integer kioskId) {
//        Integer lastOrderNumber = orderRepository.findTopByKioskIdOrderByOrderNumberDesc(kioskId);
//        return (lastOrderNumber != null) ? lastOrderNumber + 1 : 1;
//    }



}
