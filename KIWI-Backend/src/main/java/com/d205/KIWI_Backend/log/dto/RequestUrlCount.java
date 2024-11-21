package com.d205.KIWI_Backend.log.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestUrlCount {
    private List<Bucket> buckets;

    @Data
    public static class Bucket {
        private String key;
        private long doc_count;
    }
}
