package com.d205.KIWI_Backend.log.service;

import static com.d205.KIWI_Backend.global.exception.ExceptionCode.NOT_FOUND_MENU;

import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.log.domain.ViewCount;
import com.d205.KIWI_Backend.log.dto.ElasticResponse;
import com.d205.KIWI_Backend.menu.domain.Menu;
import com.d205.KIWI_Backend.menu.dto.MenuResponse;
import com.d205.KIWI_Backend.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuStatisticsService {

    private final WebClient webClient;
    private final MenuRepository menuRepository;

    @Value("${elasticsearch.url}")
    private String url;

    @Value("${elasticsearch.username}")
    private String username;

    @Value("${elasticsearch.password}")
    private String password;

    @Value("${elasticsearch.query.view-count}")
    private String viewCountQuery;

    @Value("${elasticsearch.query.view-id}")
    private String viewIdQuery;

    public List<ViewCount> getTop10Menus() {
//        String credentials = "elastic:elastic123!@#";
////        String credentials = "elastic:elastic_kiwi";
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        ElasticResponse response = webClient.post() // POST 메서드 사용
            .uri(url + "/menu_views-*/_search") // _search 엔드포인트
            .header("Authorization", "Basic " + encodedCredentials)
            .header("Content-Type", "application/json") // JSON 형식으로 요청 본문 설정
            .header("Accept", "application/json") // 응답으로 JSON 형식을 요청
//            .bodyValue(buildViewCountQuery()) // 쿼리 본문 추가
            .bodyValue(viewCountQuery)
            .retrieve()
            .bodyToMono(ElasticResponse.class)
            .block();

        if (response == null || response.getAggregations() == null || response.getAggregations().getRequest_url_count() == null) {
            return Collections.emptyList();
        }

        return response.getAggregations().getRequest_url_count().getBuckets().stream()
            .map(bucket -> ViewCount.builder()
                .requestURI(bucket.getKey())
                .viewCount((int) bucket.getDoc_count())
                .build())
            .collect(Collectors.toList());
    }
    public List<MenuResponse> suggestedMenus() {
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Elasticsearch에서 조회한 결과를 ElasticResponse로 변환
        ElasticResponse response = webClient.post()
            .uri(url + "/_search")
            .header("Authorization", "Basic " + encodedCredentials)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .bodyValue(viewIdQuery)
            .retrieve()
            .bodyToMono(ElasticResponse.class)
            .block();

        if (response == null || response.getAggregations() == null || response.getAggregations().getRequest_url_count() == null) {
            return Collections.emptyList();
        }

        // requestURI를 리스트로 추출
        List<String> requestURIs = response.getAggregations().getRequest_url_count().getBuckets().stream()
            .map(bucket -> bucket.getKey())
            .collect(Collectors.toList());

        // 각 requestURI로 메뉴 상세 조회 후 MenuResponse 리스트 생성
        return requestURIs.stream()
            .map(requestURI -> getMenuById(Integer.parseInt(requestURI)))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuResponse getMenuById(Integer id) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(NOT_FOUND_MENU));
        return MenuResponse.fromMenu(menu);
    }

}
