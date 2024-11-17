package com.d205.KIWI_Backend.order.service;

import static com.d205.KIWI_Backend.global.exception.ExceptionCode.MENU_NOT_SALES;
import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_FOUND_KIOSK_ID;
import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_FOUND_ORDER;

import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.kiosk.repository.KioskRepository;
import com.d205.KIWI_Backend.member.service.MemberService;
import com.d205.KIWI_Backend.menu.domain.Menu;
import com.d205.KIWI_Backend.menu.dto.MenuResponse;
import com.d205.KIWI_Backend.menu.repository.MenuRepository;
import com.d205.KIWI_Backend.menu.service.MenuService;
import com.d205.KIWI_Backend.order.domain.KioskOrder;
import com.d205.KIWI_Backend.order.domain.Order;
import com.d205.KIWI_Backend.order.domain.OrderMenu;
import com.d205.KIWI_Backend.order.dto.MenuSales;
import com.d205.KIWI_Backend.order.dto.OrderRequest;
import com.d205.KIWI_Backend.order.dto.OrderResponse;
import com.d205.KIWI_Backend.order.dto.OrderResponse.MenuOrderResponse;
import com.d205.KIWI_Backend.order.dto.PaymentResult;
import com.d205.KIWI_Backend.order.repository.OrderMenuRepository;
import com.d205.KIWI_Backend.order.repository.OrderRepository;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    private final OrderMenuRepository orderMenuRepository;
    private final MenuRepository menuRepository;
    private final KioskRepository kioskRepository;
    private final MemberService memberservice;
    private final Logger logger = LoggerFactory.getLogger(MenuService.class);

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMenuRepository orderMenuRepository, MenuRepository menuRepository, KioskRepository kioskRepository, MemberService memberservice) {
        this.orderRepository = orderRepository;
        this.orderMenuRepository = orderMenuRepository;
        this.menuRepository = menuRepository;
        this.kioskRepository = kioskRepository;
        this.memberservice = memberservice;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {


        // 주문 객체 생성 (빌더 패턴 사용)
        Order order = Order.builder()
            .orderDate(LocalDateTime.now())
            .age(orderRequest.getAge())
            .gender(orderRequest.getGender())
            .status("PENDING")  // 기본 상태: PENDING
            .orderNumber(orderRequest.getOrderNumber())
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
        if (kioskOptional.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_KIOSK_ID);
        }

        Kiosk kiosk = kioskOptional.get();


        KioskOrder kioskOrder = KioskOrder.builder()
            .kiosk(kiosk)
            .order(order)
            .assignedTime(LocalDateTime.now())
            .build();

        order.addKioskOrder(kioskOrder);
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
            .orderNumber(savedOrder.getOrderNumber())
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
            .orderNumber(order.getOrderNumber())
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
                .orderNumber(order.getOrderNumber())
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

    private List<MenuOrderResponse> createMenuOrderResponses(List<OrderMenu> orderMenus) {
        List<MenuOrderResponse> menuOrderResponses = new ArrayList<>();
        for (OrderMenu orderMenu : orderMenus) {
            MenuOrderResponse menuOrderResponse = MenuOrderResponse.builder()
                .menuId(orderMenu.getMenu().getId())
                .name(orderMenu.getMenu().getName())
                .quantity(orderMenu.getQuantity())
                .price(orderMenu.getMenu().getPrice())
                .build();
            menuOrderResponses.add(menuOrderResponse);
        }
        return menuOrderResponses;
    }

    // 주문에 대해 결제 상황을 반환
    public String getOrderStatus(Long kioskId) {
        String status = orderRepository.findLatestStatusByKioskId(kioskId);
        return Objects.requireNonNullElse(status, "ORDER_NOT_FOUND");
    }

    // 주문에 대해 결제 상황을 반환
    public String updateOrderStatusToCompleted(Long kioskId) {

        // 가장 최근 주문 상태가 "PENDING"인 경우에만 결제 처리
        String latestStatus = orderRepository.findLatestStatusByKioskId(kioskId);

        if (latestStatus == null || !latestStatus.equals("PENDING")) {
            // 결제할 주문이 존재하지 않거나, 주문 상태가 "PENDING"이 아닌 경우
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        int updatedCount = orderRepository.updateOrderStatusToCompleted(kioskId);

        // 주문이 없을 경우
        if (updatedCount == 0) {
            throw new BadRequestException(NOT_FOUND_KIOSK_ID);
        }
        return "SUCCESS";
    }

    // 주문에 대해 결제 상황을 반환
    public void cancelKioskOrder(Long kioskId) {
        String status = orderRepository.findLatestStatusByKioskId(kioskId);
        if (!status.equals("PENDING")) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        Long orderId = orderRepository.findLatestOrderIdByKioskId(kioskId);
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }
        orderRepository.delete(existingOrder.get());  // 주문 삭제
    }


    @Transactional
    public int calculateTotalPriceForLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Order> recentOrders = orderRepository.findByOrderDateAfter(oneMonthAgo);


        return recentOrders.stream()
            .mapToInt(order -> order.getOrderMenus().stream()
                .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
                .sum())
            .sum();
    }
    @Transactional
    public int calculateTotalPriceForLastMonthByKioskId(Integer kioskId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);


        List<Order> orders = orderRepository.findByKioskIdAndOrderDateAfter(kioskId, oneMonthAgo);

        return orders.stream()
            .flatMap(order -> order.getOrderMenus().stream())
            .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
            .sum();
    }
    @Transactional
    public Map<YearMonth, Integer> calculateMonthlyTotalForLastSixMonthsByKioskId(Integer kioskId) {
        Map<YearMonth, Integer> monthlySales = new HashMap<>();
        YearMonth currentMonth = YearMonth.now();

        // 최근 6개월 동안 각 월별로 매출 계산
        for (int i = 0; i < 6; i++) {
            YearMonth targetMonth = currentMonth.minusMonths(i);
            LocalDateTime startOfMonth = targetMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = targetMonth.atEndOfMonth().atTime(23, 59, 59);

            // 해당 월의 주문 목록 조회
            List<Order> orders = orderRepository.findByKioskIdAndOrderDateBetween(kioskId, startOfMonth, endOfMonth);


            int monthlyTotal = orders.stream()
                .flatMap(order -> order.getOrderMenus().stream())
                .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
                .sum();

            monthlySales.put(targetMonth, monthlyTotal);
        }

        return monthlySales;
    }

    @Transactional
    public int calculateTotalPriceForLastMonthByMemberId() {
        Integer memberId = memberservice.getCurrentMemberId();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Order> orders = orderRepository.findByMemberIdAndOrderDateAfter(memberId, oneMonthAgo);

        return orders.stream()
            .flatMap(order -> order.getOrderMenus().stream())
            .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
            .sum();
    }

    @Transactional
    public int calculateOrderCountForLastMonthByMemberId() {
        Integer memberId = memberservice.getCurrentMemberId();
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        // memberId로 멤버가 운영하는 키오스크 목록 조회
        List<Kiosk> kiosks = kioskRepository.findByMemberId(memberId);

        int totalOrderCount = 0;

        // 각 키오스크에 대해 한 달 동안의 주문 횟수 계산
        for (Kiosk kiosk : kiosks) {
            List<Order> orders = orderRepository.findByKioskIdAndOrderDateBetween(kiosk.getId(), startOfMonth, endOfMonth);
            totalOrderCount += orders.size();
        }

        return totalOrderCount;
    }

    @Transactional
    public Map<YearMonth, Integer> calculateMonthlyOrderCountForLastSixMonthsByMemberId() {
        Integer memberId = memberservice.getCurrentMemberId();
        Map<YearMonth, Integer> monthlyOrderCounts = new HashMap<>();
        YearMonth currentMonth = YearMonth.now();

        // memberId로 멤버가 운영하는 키오스크 목록 조회
        List<Kiosk> kiosks = kioskRepository.findByMemberId(memberId);

        // 각 키오스크에 대해 최근 6개월 간의 월별 주문 횟수 계산
        for (Kiosk kiosk : kiosks) {
            for (int i = 0; i < 6; i++) {
                YearMonth targetMonth = currentMonth.minusMonths(i);
                LocalDateTime startOfMonth = targetMonth.atDay(1).atStartOfDay();
                LocalDateTime endOfMonth = targetMonth.atEndOfMonth().atTime(23, 59, 59);

                // 해당 월의 주문 목록 조회 (키오스크 기준)
                List<Order> orders = orderRepository.findByKioskIdAndOrderDateBetween(kiosk.getId(), startOfMonth, endOfMonth);

                // 해당 월의 주문 횟수 계산
                int orderCount = orders.size();

                monthlyOrderCounts.put(targetMonth, orderCount);
            }
        }

        return monthlyOrderCounts;
    }

    @Transactional
    public List<OrderResponse> getOrdersByKioskId(Integer kioskId) {

        List<Order> orders = orderRepository.findByKioskId(kioskId);


        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderMenu> orderMenus = order.getOrderMenus();


            int totalPrice = orderMenus.stream()
                .mapToInt(orderMenu -> orderMenu.getMenu().getPrice() * orderMenu.getQuantity())
                .sum();

            // OrderResponse 객체 생성
            OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .menuOrders(createMenuOrderResponses(orderMenus))
                .kioskId(order.getKioskOrders().get(0).getKiosk().getId())
                .totalPrice(totalPrice)
                .build();

            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Transactional
    public List<MenuResponse> getTop20MenuSalesForLastMonthByMemberId() {
        Integer memberId = memberservice.getCurrentMemberId();
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        // memberId로 멤버가 운영하는 키오스크 목록 조회
        List<Kiosk> kiosks = kioskRepository.findByMemberId(memberId);

        // 메뉴별 판매 수량을 집계
        Map<Integer, Integer> menuSalesCount = new HashMap<>();

        for (Kiosk kiosk : kiosks) {
            // 해당 키오스크의 주문 목록 조회
            List<Order> orders = orderRepository.findByKioskIdAndOrderDateBetween(kiosk.getId(), startOfMonth, endOfMonth);

            // 각 주문의 메뉴 항목을 순회하며 판매 수량 집계
            for (Order order : orders) {
                for (OrderMenu orderMenu : order.getOrderMenus()) {
                    menuSalesCount.put(orderMenu.getMenu().getId(), menuSalesCount.getOrDefault(orderMenu.getMenu().getId(), 0) + orderMenu.getQuantity());
                }
            }
        }

        // 판매 수량을 기준으로 내림차순 정렬 후 상위 20개 메뉴를 리턴
        List<MenuResponse> menuResponses = menuSalesCount.entrySet().stream()
            .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue()) // 내림차순 정렬
            .limit(20) // 상위 20개만
            .map(entry -> {
                // 메뉴 정보 가져오기 (예시로 메뉴 ID를 사용하여 메뉴 정보를 조회)
                Menu menu = menuRepository.findById(entry.getKey()).orElse(null);

                // MenuResponse 객체 생성
                return menu != null ? MenuResponse.fromMenu(menu) : null;
            })
            .filter(Objects::nonNull) // null이 아닌 메뉴만 포함
            .collect(Collectors.toList());

//        if (menuResponses.isEmpty()) {
//            throw new BadRequestException(MENU_NOT_SALES);
//        }

        return menuResponses;
    }


    public Map<String, Map<String, Integer>> getTopSellingMenusByAgeGroup() {

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);

        // 쿼리 호출 - 전체 주문 조회
        List<Object[]> result = orderRepository.findTopSellingMenusByAgeGroup(startDate, endDate);

        Map<String, Map<String, Integer>> ageGroupSales = new HashMap<>();

        for (Object[] row : result) {
            String menuName = (String) row[0];
            Integer totalSales = ((Number) row[1]).intValue();
            Integer age = (Integer) row[2];

            if (age == null) {
                age = 0; // 기본값 0으로 설정, 혹은 원하는 값으로 변경
            }


            String ageGroup = getAgeGroup(age);

            // 해당 연령대에 대한 메뉴 판매량이 이미 기록되어 있으면, 더 높은 판매량만 저장
            ageGroupSales.putIfAbsent(ageGroup, new HashMap<>());
            Map<String, Integer> menuSales = ageGroupSales.get(ageGroup);

            // 이미 해당 메뉴가 존재하는 경우, 판매량 비교 후 더 높은 판매량을 저장
            if (menuSales.containsKey(menuName)) {
                Integer currentSales = menuSales.get(menuName);
                if (totalSales > currentSales) {
                    menuSales.put(menuName, totalSales); // 더 많이 팔린 경우 교체
                }
            } else {
                menuSales.put(menuName, totalSales); // 메뉴가 없으면 추가
            }
        }

        // 각 연령대별로 가장 많이 팔린 메뉴만 남기기
        Map<String, Map<String, Integer>> topSellingMenus = new HashMap<>();
        for (String ageGroup : ageGroupSales.keySet()) {
            Map<String, Integer> menuSales = ageGroupSales.get(ageGroup);

            // 연령대별 가장 많이 팔린 메뉴만 추출
            String topMenu = null;
            Integer topSales = 0;
            for (Map.Entry<String, Integer> entry : menuSales.entrySet()) {
                if (entry.getValue() > topSales) {
                    topSales = entry.getValue();
                    topMenu = entry.getKey();
                }
            }

            // 가장 많이 팔린 메뉴만 저장
            if (topMenu != null) {
                topSellingMenus.put(ageGroup, Map.of(topMenu, topSales));
            }
        }

        return topSellingMenus;
    }

    public Map<String, Map<String, Integer>> getTopSellingMenusByAgeGroupByKioskId(Integer kioskId) {

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(1);

        List<Object[]> result = orderRepository.findTopSellingMenusByAgeGroupByKioskId(kioskId, startDate, endDate);

        Map<String, Map<String, Integer>> ageGroupSales = new HashMap<>();

        for (Object[] row : result) {
            String menuName = (String) row[0];
            Integer totalSales = ((Number) row[1]).intValue();
            Integer age = (Integer) row[2];

            if (age == null) {
                age = 0; // 기본값 0으로 설정, 혹은 원하는 값으로 변경
            }

            String ageGroup = getAgeGroup(age);

            // 해당 연령대에 대한 메뉴 판매량이 이미 기록되어 있으면, 더 높은 판매량만 저장
            ageGroupSales.putIfAbsent(ageGroup, new HashMap<>());
            Map<String, Integer> menuSales = ageGroupSales.get(ageGroup);


            if (menuSales.containsKey(menuName)) {
                Integer currentSales = menuSales.get(menuName);
                if (totalSales > currentSales) {
                    menuSales.put(menuName, totalSales); // 더 많이 팔린 경우 교체
                }
            } else {
                menuSales.put(menuName, totalSales); // 메뉴가 없으면 추가
            }
        }


        Map<String, Map<String, Integer>> topSellingMenus = new HashMap<>();
        for (String ageGroup : ageGroupSales.keySet()) {
            Map<String, Integer> menuSales = ageGroupSales.get(ageGroup);

            String topMenu = null;
            Integer topSales = 0;
            for (Map.Entry<String, Integer> entry : menuSales.entrySet()) {
                if (entry.getValue() > topSales) {
                    topSales = entry.getValue();
                    topMenu = entry.getKey();
                }
            }
            // 가장 많이 팔린 메뉴만 저장
            if (topMenu != null) {
                topSellingMenus.put(ageGroup, Map.of(topMenu, topSales));
            }
        }

        return topSellingMenus;
    }

    @Transactional
    public Map<String, List<MenuSales>> getTopSoldMenusByMemberId(Integer memberId) {

        List<Order> orders = orderRepository.findOrdersByMemberId(memberId);


        Map<String, Map<Integer, Integer>> ageGroupSales = new HashMap<>();

        // 연령대 구분
        String[] ageGroups = {"10대", "20대", "30대", "40대", "50대 이상"};

        for (Order order : orders) {

            int age = (order.getAge() != null) ? order.getAge() : 0;

            String ageGroup = getAgeGroup(age); // 연령대 계산


            for (OrderMenu orderMenu : order.getOrderMenus()) {
                Integer menuId = orderMenu.getMenu().getId();
                Integer quantity = orderMenu.getQuantity();


                ageGroupSales
                    .computeIfAbsent(ageGroup, k -> new HashMap<>())
                    .merge(menuId, quantity, Integer::sum);
            }
        }

        Map<String, List<MenuSales>> topSoldMenusByAgeGroup = new HashMap<>();

        for (String ageGroup : ageGroups) {
            Map<Integer, Integer> menuSales = ageGroupSales.getOrDefault(ageGroup, Collections.emptyMap());


            List<MenuSales> sortedMenuSales = menuSales.entrySet().stream()
                .map(entry -> {

                    Optional<Menu> menuOptional = menuRepository.findById(entry.getKey());
                    String menuName = menuOptional.map(Menu::getName).orElse("Unknown");
                    return new MenuSales(entry.getKey(), menuName, entry.getValue());
                })
                .sorted(Comparator.comparingInt(MenuSales::getSales).reversed())
                .collect(Collectors.toList());

            List<MenuSales> topMenus = sortedMenuSales.stream()
                .limit(1)
                .collect(Collectors.toList());

            topSoldMenusByAgeGroup.put(ageGroup, topMenus);
        }

        return topSoldMenusByAgeGroup;
    }

    @Transactional
    public Map<String, List<MenuSales>> getTopSoldMenusByLoginMember() {
        Integer memberId = memberservice.getCurrentMemberId();

        List<Order> orders = orderRepository.findOrdersByMemberId(memberId);


        Map<String, Map<Integer, Integer>> ageGroupSales = new HashMap<>();

        // 연령대 구분
        String[] ageGroups = {"10대", "20대", "30대", "40대", "50대 이상"};

        for (Order order : orders) {

            int age = (order.getAge() != null) ? order.getAge() : 0;

            String ageGroup = getAgeGroup(age); // 연령대 계산


            for (OrderMenu orderMenu : order.getOrderMenus()) {
                Integer menuId = orderMenu.getMenu().getId();
                Integer quantity = orderMenu.getQuantity();


                ageGroupSales
                    .computeIfAbsent(ageGroup, k -> new HashMap<>())
                    .merge(menuId, quantity, Integer::sum);
            }
        }

        Map<String, List<MenuSales>> topSoldMenusByAgeGroup = new HashMap<>();

        for (String ageGroup : ageGroups) {
            Map<Integer, Integer> menuSales = ageGroupSales.getOrDefault(ageGroup, Collections.emptyMap());


            List<MenuSales> sortedMenuSales = menuSales.entrySet().stream()
                .map(entry -> {

                    Optional<Menu> menuOptional = menuRepository.findById(entry.getKey());
                    String menuName = menuOptional.map(Menu::getName).orElse("Unknown");
                    return new MenuSales(entry.getKey(), menuName, entry.getValue());
                })
                .sorted(Comparator.comparingInt(MenuSales::getSales).reversed())
                .collect(Collectors.toList());

            List<MenuSales> topMenus = sortedMenuSales.stream()
                .limit(1)
                .collect(Collectors.toList());

            topSoldMenusByAgeGroup.put(ageGroup, topMenus);
        }

        return topSoldMenusByAgeGroup;
    }


    public Map<YearMonth, Integer> getMonthlySalesForLastSixMonthsByMemberId(Integer memberId) {
        // 현재 월 기준 지난 6개월의 YearMonth 계산
        YearMonth currentMonth = YearMonth.now();
        List<YearMonth> lastSixMonths = IntStream.range(0, 6)
            .mapToObj(currentMonth::minusMonths)
            .collect(Collectors.toList());

        Map<YearMonth, Integer> monthlySales = new HashMap<>();

        for (YearMonth month : lastSixMonths) {
            LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);

            // 각 월별 데이터를 조회하여 결과를 매핑
            Integer totalSales = orderRepository.findMonthlySalesByMemberIdForLastSixMonths(memberId, startOfMonth, endOfMonth)
                .stream()
                .map(data -> ((Number) data[2]).intValue())
                .findFirst()
                .orElse(0);

            monthlySales.put(month, totalSales);
        }

        return monthlySales;
    }

    @Transactional
    public Map<YearMonth, Integer> calculateOrderCountForLastSixMonthsByMemberId(Integer memberId) {
        YearMonth currentMonth = YearMonth.now();

        // 6개월 전부터 현재까지의 6개월 동안 각 달을 구합니다.
        List<YearMonth> lastSixMonths = IntStream.range(0, 6)
            .mapToObj(currentMonth::minusMonths)
            .collect(Collectors.toList());

        // 멤버가 운영하는 키오스크 목록을 조회합니다.
        List<Kiosk> kiosks = kioskRepository.findByMemberId(memberId);

        // 월별 주문 횟수를 저장할 Map (초기화: 모든 월은 0으로 설정)
        Map<YearMonth, Integer> monthlyOrderCount = lastSixMonths.stream()
            .collect(Collectors.toMap(month -> month, month -> 0));

        // 각 키오스크에 대해 6개월 동안의 주문 횟수를 계산합니다.
        for (Kiosk kiosk : kiosks) {
            for (YearMonth month : lastSixMonths) {
                LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
                LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);

                // 키오스크별로 해당 월의 주문을 조회합니다.
                List<Order> orders = orderRepository.findByKioskIdAndOrderDateBetween(kiosk.getId(), startOfMonth, endOfMonth);
                int orderCount = orders.size();

                // 월별 주문 횟수를 누적합니다.
                monthlyOrderCount.put(month, monthlyOrderCount.getOrDefault(month, 0) + orderCount);
            }
        }

        return monthlyOrderCount;
    }

    //TODO : 이거 대신 orderid로 주문 찾는거를 kioskId로 주문찾는거로 변동 후 메뉴 아이템 출력하는 객체를 컨버팅해서 해결하는방식으로 수정해서해보기
    public List<PaymentResult> getLatestOrderMenu(int kioskId) {
        return orderMenuRepository.findLatestOrderMenu(kioskId);
    }
    // 주문에 대해 결제 상황을 반환
    public List<MenuOrderResponse> getLatestOrderMenuByKioskId(Long kioskId) {
        String status = orderRepository.findLatestStatusByKioskId(kioskId);

        if (status.equals("PENDING")) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        Long orderId = orderRepository.findLatestOrderIdByKioskId(kioskId);
        Optional<Order> existingOrder = orderRepository.findById(orderId);
        if (existingOrder.isEmpty()) {
            throw new BadRequestException(NOT_FOUND_ORDER);
        }

        Order order = existingOrder.get();
        List<OrderMenu> orderMenus = order.getOrderMenus(); // 기존 메뉴 항목
        return createMenuOrderResponses(orderMenus);
    }



//    @Transactional
//    public Map<YearMonth, Integer> calculateOrderCountForLastSixMonthsByMemberId(Integer memberId) {
//        YearMonth currentMonth = YearMonth.now();
//
//        // 6개월 전부터 현재까지의 6개월 동안 각 달을 구합니다.
//        List<YearMonth> lastSixMonths = IntStream.range(0, 6)
//            .mapToObj(currentMonth::minusMonths)
//            .collect(Collectors.toList());
//
//        // 멤버가 운영하는 키오스크 목록을 조회합니다.
//        List<Kiosk> kiosks = kioskRepository.findByMemberId(memberId);
//
//        // 월별 주문 횟수를 저장할 Map
//        Map<YearMonth, Integer> monthlyOrderCount = new HashMap<>();
//
//        // 각 키오스크에 대해 6개월 동안의 주문 횟수를 계산합니다.
//        for (Kiosk kiosk : kiosks) {
//            for (YearMonth month : lastSixMonths) {
//                LocalDateTime startOfMonth = month.atDay(1).atStartOfDay();
//                LocalDateTime endOfMonth = month.atEndOfMonth().atTime(23, 59, 59);
//
//                // 키오스크별로 해당 월의 주문을 조회합니다.
//                List<Order> orders = orderRepository.findByKioskIdAndOrderDateBetween(kiosk.getId(), startOfMonth, endOfMonth);
//                int orderCount = orders.size();
//
//                // 월별 주문 횟수를 누적합니다.
//                monthlyOrderCount.put(month, monthlyOrderCount.getOrDefault(month, 0) + orderCount);
//            }
//        }
//
//        return monthlyOrderCount;
//    }

    private String getAgeGroup(Integer age) {
        if (age >= 10 && age < 20) {
            return "10대";
        } else if (age >= 20 && age < 30) {
            return "20대";
        } else if (age >= 30 && age < 40) {
            return "30대";
        } else if (age >= 40 && age < 50) {
            return "40대";
        } else {
            return "50대 이상";
        }
    }


}
