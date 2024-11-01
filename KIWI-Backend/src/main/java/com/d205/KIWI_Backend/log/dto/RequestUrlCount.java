package com.d205.KIWI_Backend.log.dto;

import java.util.List;
import lombok.Data;

@Data
public class RequestUrlCount {

    private int doc_count_error_upper_bound;
    private int sum_other_doc_count;
    private List<Bucket> buckets;

    // Getters and Setters
}
