package com.fourMan.GlobalAssets.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class RateTwelveClient {

    private static final Logger log = LoggerFactory.getLogger(RateTwelveClient.class);

    // 이름 충돌 방지를 위해 풀패스 사용
    @org.springframework.beans.factory.annotation.Value("${twelve.base-url}")
    private String twelveBaseUrl;

    @org.springframework.beans.factory.annotation.Value("${twelve.api-key}")
    private String twelveApiKey;

    private RestTemplate rt() {
        RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return rt;
    }

    /** TwelveData에서 최근 n일(최신→과거) 종가만 가져오기 */
    public List<Bar> fetchDailySeries(String symbol, int days) {
        String url = String.format(
                "%s/time_series?symbol=%s&interval=1day&outputsize=%d&apikey=%s",
                twelveBaseUrl, symbol, days, twelveApiKey
        );

        TimeSeriesResp resp = rt().getForObject(url, TimeSeriesResp.class);
        if (resp == null || resp.values == null || resp.values.isEmpty()) {
            log.warn("[TwelveData] empty response for {}", symbol);
            return List.of();
        }

        List<Bar> out = new ArrayList<>();
        for (TSValue v : resp.values) {
            LocalDate d = LocalDate.parse(v.datetime.substring(0, 10));
            BigDecimal close = new BigDecimal(v.close);
            out.add(new Bar(d, close));
        }
        return out; // 최신순으로 반환
    }

    // ---- 응답 파싱용 ----
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class TimeSeriesResp {
        @JsonProperty("values") List<TSValue> values;
    }

    // 이름을 Value -> TSValue 로 변경
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class TSValue {
        @JsonProperty("datetime") String datetime;
        @JsonProperty("close") String close;
    }

    // 서비스에서 쓰는 최소 바 타입
    public record Bar(LocalDate date, BigDecimal close) {}
}
