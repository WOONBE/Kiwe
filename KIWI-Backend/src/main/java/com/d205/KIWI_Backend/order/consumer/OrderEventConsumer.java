package com.d205.KIWI_Backend.order.consumer;// package com.d205.KIWI_Backend.kafka;

import com.d205.KIWI_Backend.menu.domain.Menu;
import com.d205.KIWI_Backend.menu.repository.MenuRepository; // MenuRepository가 있다고 가정
import com.d205.KIWI_Backend.order.domain.Order;
import com.d205.KIWI_Backend.order.domain.OrderMenu;
import com.d205.KIWI_Backend.order.dto.OrderEventDto;
import com.d205.KIWI_Backend.order.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository; // 메뉴 정보를 가져오기 위함

    @KafkaListener(topics = "order-topic", groupId = "order-processor-group")
    @Transactional
    public void consume(OrderEventDto orderDto) {
        log.info("Consumed message: {}", orderDto.toString());

        try {

            Order order = Order.builder()
                .orderDate(orderDto.getOrderDate())
                .age(orderDto.getCustomerInfo().getAge())
                .gender(orderDto.getCustomerInfo().getGender())
                .kioskId(orderDto.getKioskId())
                .status("COMPLETED") // 상태 지정
                .build();


            List<OrderMenu> orderMenus = orderDto.getOrderItems().stream()
                .map(itemDto -> {
                    // menuId로 Menu 엔티티를 조회 (실제로는 예외처리 필요)
                    Menu menu = menuRepository.findById(itemDto.getMenuId())
                        .orElseThrow(() -> new RuntimeException("Menu not found: " + itemDto.getMenuId()));
                    
                    return OrderMenu.builder()
                        .order(order) // 연관관계 설정
                        .menu(menu)
                        .quantity(itemDto.getQuantity())
                        .build();
                })
                .collect(Collectors.toList());
            

            order.addOrderMenus(orderMenus);


            orderRepository.save(order);
            log.info("Order (kioskId: {}) has been saved to the database.", order.getKioskId());

        } catch (Exception e) {

            log.error("Failed to save order to DB. data: {}, error: {}", orderDto.toString(), e.getMessage());
        }
    }
}