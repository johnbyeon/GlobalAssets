package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.UpbitMinuteCandleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UpbitCandleClientService {

    private static final String BASE = "https://api.upbit.com/v1/candles/minutes";
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_INSTANT;

    private final RestTemplate restTemplate;



    public UpbitMinuteCandleDto[] fetchOneMinuteAgo(String market, int unit) {
        // 1분 전, 분 단위로 내림(캔들은 분 경계 기준이 깔끔함)
        var toUtc = ZonedDateTime.now(ZoneOffset.UTC)
                .minusMinutes(1)
                .truncatedTo(ChronoUnit.MINUTES)
                .toInstant()
                .toString(); // ISO8601 (예: 2025-09-10T08:16:00Z)

        URI uri = UriComponentsBuilder
                .fromHttpUrl(BASE + "/" + unit)
                .queryParam("market", market)        // 예: KRW-BTC
                .queryParam("to", toUtc)             // 1분 전 시각(UTC)
                .queryParam("count", 1)              // 1개만
                .build(true)                         // 인코딩 보존
                .toUri();

        return restTemplate.getForObject(uri, UpbitMinuteCandleDto[].class);
    }
}
