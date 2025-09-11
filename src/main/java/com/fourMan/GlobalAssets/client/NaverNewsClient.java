package com.fourMan.GlobalAssets.client;

import com.fourMan.GlobalAssets.dto.NewsItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RequiredArgsConstructor
public class NaverNewsClient {

    @Qualifier("naverRestTemplate")
    private final RestTemplate restTemplate;

    @Value("${naver.search.news-url}")
    private String baseUrl;

    @SuppressWarnings("unchecked")
    public List<NewsItemDto> search(String query, int limit) {
        int display = Math.max(1, Math.min(100, limit));

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("query", query)
                .queryParam("display", display)
                .queryParam("sort", "date")
                .build(true)
                .toUri();

        Map<String, Object> res = restTemplate.getForObject(uri, Map.class);
        if (res == null) return List.of();

        // <-- 핵심: 안전하게 Object -> List<?>로 받고 요소마다 Map으로 캐스팅
        Object itemsObj = res.get("items");
        List<?> rawItems = (itemsObj instanceof List) ? (List<?>) itemsObj : Collections.emptyList();

        List<NewsItemDto> out = new ArrayList<>();
        int no = 1;
        for (Object o : rawItems) {
            if (!(o instanceof Map)) continue;
            Map<String, Object> it = (Map<String, Object>) o;

            String title = String.valueOf(it.getOrDefault("title", ""));
            String link  = String.valueOf(it.getOrDefault("link",  ""));
            String pub   = String.valueOf(it.getOrDefault("pubDate", ""));

            // 네이버가 주는 <b>태그 제거
            title = title.replaceAll("<[^>]+>", "");

            String timeText = pub;
            try {
                DateTimeFormatter f = DateTimeFormatter.RFC_1123_DATE_TIME;
                timeText = OffsetDateTime.parse(pub, f)
                        .toLocalDateTime()
                        .toString()
                        .replace('T', ' ');
            } catch (Exception ignore) { }

            out.add(new NewsItemDto(no++, title, link, timeText));
        }
        return out;
    }
}
