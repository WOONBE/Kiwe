package com.d205.KIWI_Backend.order.producer;// package com.d205.KIWI_Backend.kafka;

import com.d205.KIWI_Backend.order.dto.OrderEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final String TOPIC = "order-topic";
    private final KafkaTemplate<String, OrderEventDto> kafkaTemplate;

    public void sendOrder(OrderEventDto orderDto) {
        System.out.println(String.format("Produce message : %s", orderDto.toString()));
        // 'order-topic'으로 DTO 객체를 전송
        this.kafkaTemplate.send(TOPIC, orderDto);
    }
}