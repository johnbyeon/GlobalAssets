package com.fourMan.GlobalAssets.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RateTwelveDataClient {

    private final RestTemplate restTemplate;

    @Value("${twelvedata.base-url}")
    private String baseUrl;

    @Value("${twelvedata.apikey}")
    private String apiKey;

    @Value("${twelvedata.outputsize:15}")
    private int outputSize;

    // 내부 클래스 이름을 'Value' -> 'TdBar'로 변경하여 @Value 애노테이션과의 이름 충돌을 해결
    public List<TdBar> fetchDailySeries(String symbol) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/time_series")
                .queryParam("symbol", symbol)
                .queryParam("interval", "1day")
                .queryParam("outputsize", outputSize)
                .queryParam("order", "desc")
                .queryParam("timezone", "Asia/Seoul")
                .queryParam("apikey", apiKey)
                .toUriString();

        Response r = restTemplate.getForObject(url, Response.class);
        if (r == null || r.values == null) {
            throw new IllegalStateException("Empty response for " + symbol);
        }
        return r.values;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private List<TdBar> values;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TdBar {
        private String datetime;
        private BigDecimal open;
        private BigDecimal close;
        private BigDecimal high;
        private BigDecimal low;

        public LocalDate date() { return LocalDate.parse(datetime); }
    }
}
