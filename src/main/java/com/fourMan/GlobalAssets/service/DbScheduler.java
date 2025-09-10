package com.fourMan.GlobalAssets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class DbScheduler {

    private final PriceIngestService priceIngest;
    private final DailySummaryService dailySummary;

    private static final String[][] TARGETS = {
            {"KRW-BTC","비트코인"},
            {"KRW-ETH","이더리움"},
            {"KRW-XRP","리플"},
            {"KRW-SOL","솔라나"},
            {"KRW-DOGE","도지코인"}
    };

    // 1분봉 적재
    @Scheduled(cron = "*/20 * * * * *") // 20초마다 최근 3개
    public void pull1m() {
        for (String[] t : TARGETS) {
            try {
                int n = priceIngest.fetchAndStoreMinuteCandles(t[0], "1m", 3, t[1]);
                if (n > 0) log.info("[prices] {} +{}", t[0], n);
            } catch (Exception e) {
                log.warn("[prices] {} failed: {}", t[0], e.toString());
            }
        }
    }

    // 일일 요약 갱신
    @Scheduled(cron = "0 */5 * * * *") // 5분마다
    public void daily() {
        for (String[] t : TARGETS) {
            try {
                dailySummary.refreshDaily(t[0], t[1]);
            } catch (Exception e) {
                log.warn("[daily] {} failed: {}", t[0], e.toString());
            }
        }
    }
}
