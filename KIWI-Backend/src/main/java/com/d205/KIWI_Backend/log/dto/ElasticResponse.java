package com.d205.KIWI_Backend.log.dto;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ElasticResponse {

    private int took;
    private boolean timed_out;
    private Shards _shards;
    private Hits hits;
    private Aggregations aggregations;

    // Getters and Setters
}

