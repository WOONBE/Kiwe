package com.d205.KIWI_Backend.log.dto;

import java.util.List;
import lombok.Data;

@Data
public class Hits {

    private Total total;
    private List<Hit> hits;

    // Getters and Setters
}
