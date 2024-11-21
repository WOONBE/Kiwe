package com.d205.KIWI_Backend.log.dto;

import java.util.Map;
import lombok.Data;

@Data
public class Hit {

    private String _index;
    private String _id;
    private double _score;
    private Map<String, Object> _source;  // _source는 다양한 타입의 데이터가 들어가므로 Map으로 처리

    // Getters and Setters
}
