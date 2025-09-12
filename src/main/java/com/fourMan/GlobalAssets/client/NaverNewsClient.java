package com.fourMan.GlobalAssets.client;

import com.fourMan.GlobalAssets.dto.NewsItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverNewsClient {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    private static final String NEWS_URL = "https://openapi.naver.com/v1/search/news.json";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 네이버 뉴스 검색
     *
     * @param query 검색어
     * @param limit 가져올 건수
     * @return 뉴스 리스트
     */
    public List<NewsItemDto> search(String query, int limit) {
        try {
            // 반드시 UTF-8 인코딩
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder.fromHttpUrl(NEWS_URL)
                    .queryParam("query", encodedQuery)
                    .queryParam("display", limit)
                    .queryParam("sort", "date") // 최신순
                    .build(true) // true = 인코딩 유지
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Naver-Client-Id", clientId);
            headers.add("X-Naver-Client-Secret", clientSecret);

            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                log.warn("[NaverNews] Empty response for query={}", query);
                return List.of();
            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
            if (items == null) {
                return List.of();
            }

            List<NewsItemDto> result = new ArrayList<>();
            for (Map<String, Object> item : items) {
                NewsItemDto dto = new NewsItemDto();
                dto.setTitle((String) item.get("title"));
                dto.setLink((String) item.get("link"));
                dto.setTitle((String) item.get("title"));
                dto.setTime((String) item.get("pubDate"));
                result.add(dto);
            }
            return result;

        } catch (Exception e) {
            log.error("[NaverNews] Search failed for query={}", query, e);
            return List.of();
        }
    }
}
