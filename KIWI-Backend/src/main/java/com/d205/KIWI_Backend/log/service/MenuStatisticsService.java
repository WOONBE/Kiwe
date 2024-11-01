package com.d205.KIWI_Backend.log.service;

import com.d205.KIWI_Backend.log.domain.ViewCount;
import com.d205.KIWI_Backend.log.dto.ElasticResponse;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class MenuStatisticsService {

    private final WebClient webClient;

    @Value("${elasticsearch.query.view-count}") // 조회 수 쿼리
    private String viewCountQuery;
    @Value("${elasticsearch.url}")
    private String url;

    public List<ViewCount> getMenuViewCounts() {
        String credentials = "elastic:elastic123!@#";
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        // ElasticSearch에서 응답을 받음
        ElasticResponse response = webClient.post()
            .uri(url)
            .header("content-type", "application/json")
            .header("Authorization", "Basic " + encodedCredentials)
            .bodyValue(viewCountQuery)
            .retrieve()
            .bodyToMono(ElasticResponse.class)
            .block(); // 응답을 기다림

        // request_url_count로 접근
        return response.getAggregations().getRequest_url_count().getBuckets().stream()
            .map(bucket -> ViewCount.builder()
                .requestURI(bucket.getKey())
                .viewCount(bucket.getDoc_count())
                .build())
            .collect(Collectors.toList());
    }
}
