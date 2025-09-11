package com.fourMan.GlobalAssets.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Component
public class RateNaverSearchClient {

    private final RestTemplate rest = new RestTemplate();

    @Value("${naver.search.client-id:}")
    private String clientId;

    @Value("${naver.search.client-secret:}")
    private String clientSecret;

    /**
     * 서비스에서 사용하는 간단한 시그니처
     */
    public NaverNewsResponse searchNews(String query, int display) {
        return searchNews(query, 1, display, "date");
    }

    /**
     * 필요시 start/sort까지 지정 가능한 오버로드
     */
    public NaverNewsResponse searchNews(String query, int start, int display, String sort) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://openapi.naver.com/v1/search/news.json")
                .queryParam("query", query)
                .queryParam("start", Math.max(1, start))
                .queryParam("display", Math.max(1, Math.min(display, 100))) // 네이버 제한
                .queryParam("sort", (sort == null || sort.isBlank()) ? "date" : sort)
                .toUriString();

        var headers = new org.springframework.http.HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<Void> req = new HttpEntity<>(headers);
        ResponseEntity<NaverNewsResponse> res =
                rest.exchange(url, HttpMethod.GET, req, NaverNewsResponse.class);

        NaverNewsResponse body = res.getBody();
        if (body == null) {
            body = new NaverNewsResponse();
            body.setItems(Collections.emptyList());
        } else if (body.getItems() == null) {
            body.setItems(Collections.emptyList());
        }
        return body;
    }

    @Data
    public static class NaverNewsResponse {
        private List<NaverItem> items;
    }

    @Data
    public static class NaverItem {
        private String title;
        private String link;
        private String pubDate;
        // 필요하면 description, originallink 등도 추가 가능
    }
}
