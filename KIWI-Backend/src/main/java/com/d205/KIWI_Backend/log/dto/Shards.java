package com.d205.KIWI_Backend.log.dto;

import lombok.Data;

@Data
public class Shards {

    private int total;
    private int successful;
    private int skipped;
    private int failed;

    // Getters and Setters
}
