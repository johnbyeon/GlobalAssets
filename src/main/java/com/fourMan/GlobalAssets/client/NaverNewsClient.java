package com.fourMan.GlobalAssets.client;

import com.fourMan.GlobalAssets.dto.NewsItemDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class NaverNewsClient {

    private final RestTemplate restTemplate;
    private final String newsUrl;
    private final String clientId;
    private final String clientSecret;

    public NaverNewsClient(
            @Qualifier("naverRestTemplate") RestTemplate restTemplate,
            @Value("${naver.search.news-url}") String newsUrl,
            @Value("${naver.search.client-id}") String clientId,
            @Value("${naver.search.client-secret}") String clientSecret
    ) {
        this.restTemplate = restTemplate;
        this.newsUrl = newsUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public List<NewsItemDto> search(String query, int limit) {
        // 한글/괄호 포함 → 반드시 인코딩
        URI uri = UriComponentsBuilder.fromHttpUrl(newsUrl)
                .queryParam("query", query)
                .queryParam("display", Math.min(limit, 30))
                .queryParam("sort", "date")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", clientId);
        headers.add("X-Naver-Client-Secret", clientSecret);

        ResponseEntity<Map<String, Object>> res = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> body = res.getBody();
        List<?> items = (body != null) ? (List<?>) body.getOrDefault("items", List.of()) : List.of();

        List<NewsItemDto> out = new ArrayList<>();
        int no = 1;
        for (Object o : items) {
            Map<?, ?> m = (Map<?, ?>) o;

            // getOrDefault는 제네릭 CAP 충돌 → 안전하게 get() 후 null 체크
            String link  = Objects.toString(m.get("link"), "");
            String title = stripTags(Objects.toString(m.get("title"), ""));
            String time  = Objects.toString(m.get("pubDate"), "");

            out.add(new NewsItemDto(no++, title, link, time));
        }
        return out;
    }

    private String stripTags(String s) {
        if (s == null) return "";
        // 네이버 검색 결과에 종종 붙는 <b>태그 제거 & HTML 엔티티 간단 정리
        String noTags = s.replaceAll("<[^>]*>", "");
        return noTags
                .replace("&quot;", "\"")
                .replace("&apos;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">");
    }
}
