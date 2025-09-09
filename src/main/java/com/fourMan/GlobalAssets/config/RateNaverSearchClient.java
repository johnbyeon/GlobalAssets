package com.fourMan.GlobalAssets.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateNaverSearchClient {

    private final RestTemplate restTemplate;

    @Value("${naver.search.news-url}")
    private String newsUrl;

    @Value("${naver.search.client-id}")
    private String clientId;

    @Value("${naver.search.client-secret}")
    private String clientSecret;

    /** 네이버 뉴스 검색 호출(오류 시 예외 전파 없이 빈 결과) */
    public Response searchNews(String query, int display, int start, String sort) {
        if (isBlank(clientId) || isBlank(clientSecret)) {
            log.warn("[NAVER] Missing ClientId/Secret. Return empty. query={}", query);
            return emptyResponse();
        }
        try {
            String url = UriComponentsBuilder.fromHttpUrl(newsUrl)
                    .queryParam("query", query)
                    .queryParam("display", Math.max(1, Math.min(display, 100)))
                    .queryParam("start", Math.max(1, Math.min(start, 1000)))
                    .queryParam("sort", (sort == null || sort.isBlank()) ? "date" : sort)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<Response> resp =
                    restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Response.class);

            return resp.getBody() != null ? resp.getBody() : emptyResponse();

        } catch (RestClientResponseException e) {
            log.warn("[NAVER] API error. status={} body={}", e.getRawStatusCode(), safe(e.getResponseBodyAsString()));
            return emptyResponse();
        } catch (Exception e) {
            log.error("[NAVER] API call failed. query={}", query, e);
            return emptyResponse();
        }
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private String safe(String s) { return s == null ? "" : s; }

    private Response emptyResponse() {
        Response r = new Response();
        r.setItems(List.of());
        r.setDisplay(0);
        r.setStart(1);
        r.setTotal(0);
        return r;
    }

    /* ==== JSON DTO ==== */
    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private String lastBuildDate;
        private int total;
        private int start;
        private int display;
        private List<Item> items;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String title;        // <b>…</b> 포함
        private String originallink;
        private String link;
        private String description;
        private String pubDate;      // RFC 822
    }
}
