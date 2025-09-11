package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.DailySummaryDto;
import com.fourMan.GlobalAssets.service.RateFxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rate/api")
@RequiredArgsConstructor
@Slf4j
public class RateFxController {

    private final RateFxService fxService;

    // 단일 심볼: /rate/api/recent-daily?symbol=USD/KRW&limit=15
    @GetMapping("/recent-daily")
    public List<DailySummaryDto> recentDaily(@RequestParam("symbol") String symbol,
                                             @RequestParam(value = "limit", defaultValue = "15") int limit) {
        return fxService.recentDaily(symbol, limit);
    }

    // 여러 심볼 묶음: /rate/api/recent-daily/bundle
    //                /rate/api/recent-daily/bundle?symbols=USD/KRW,JPY/KRW&limit=15
    @GetMapping("/recent-daily/bundle")
    public Map<String, List<DailySummaryDto>> recentDailyBundle(
            @RequestParam(value = "symbols", required = false) String symbols,
            @RequestParam(value = "limit", defaultValue = "15") int limit) {

        List<String> list = (symbols == null || symbols.isBlank())
                ? List.of("USD/KRW", "JPY/KRW", "CNY/KRW", "EUR/KRW")
                : Arrays.stream(symbols.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        Map<String, List<DailySummaryDto>> out = new LinkedHashMap<>();
        for (String s : list) {
            try {
                out.put(s, fxService.recentDaily(s, limit));
            } catch (Exception e) {
                log.warn("skip {} - {}", s, e.getMessage());
                out.put(s, List.of());
            }
        }
        return out;
    }
}
