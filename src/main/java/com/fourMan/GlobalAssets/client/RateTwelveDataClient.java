package com.fourMan.GlobalAssets.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class RateTwelveDataClient {

    private final RestTemplate rest = new RestTemplate();

    @Value("${twelve.api-key:}")
    private String apiKey;

    /** 관리자에서 원문 JSON 확인용 */
    public String fetchDailySeriesRaw(String symbol, int size, String key) {
        String useKey = (key != null && !key.isBlank()) ? key : apiKey;

        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.twelvedata.com/time_series")
                .queryParam("symbol", symbol)
                .queryParam("interval", "1day")
                .queryParam("outputsize", size)
                .queryParam("apikey", useKey)
                .toUriString();

        return rest.getForObject(url, String.class);
    }

    /** 날짜/종가만 간단히 반환 (지금은 파싱 미구현, 시그니처만 맞춰 둠) */
    public List<Bar> fetchDailySeries(String symbol, int size, String key) {
        // TODO: fetchDailySeriesRaw(...) 결과 JSON을 Jackson 등으로 파싱해서 Bar 리스트로 변환
        // 현재는 컴파일/런 안정화를 위해 빈 리스트 반환
        return List.of();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bar {
        private LocalDate date;
        private BigDecimal close;
    }
}
