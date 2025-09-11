package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.DailySummaryDto;
import com.fourMan.GlobalAssets.service.RateFxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fx")
@RequiredArgsConstructor
public class RateFxApiController {

    private final RateFxService service;

    /** 수집: 예) /api/fx/ingest?symbol=USD/KRW&days=15 */
    @PostMapping("/ingest")
    public String ingest(@RequestParam String symbol,
                         @RequestParam(defaultValue = "15") int days) {
        service.ingest(symbol, days);
        return "ok";
    }


    // 추가: 브라우저에서 쉽게 테스트하려고 GET도 허용
    @GetMapping("/ingest")
    public String ingestGet(@RequestParam String symbol,
                            @RequestParam(defaultValue = "20") int days) {
        service.ingest(symbol, days);
        return "ok";
    }

    /** 최근 n건: 예) /api/fx/recent?symbol=USD/KRW&limit=10 */
    @GetMapping("/recent")
    public List<DailySummaryDto> recentOne(@RequestParam String symbol,
                                           @RequestParam(defaultValue = "10") int limit) {
        return service.recentOne(symbol, limit);
    }

    /** 여러 심볼: 예) /api/fx/recent-many?symbols=USD/KRW,EUR/KRW&limit=10 */
    @GetMapping("/recent-many")
    public Map<String, List<DailySummaryDto>> recentMany(@RequestParam String symbols,
                                                         @RequestParam(defaultValue = "10") int limit) {
        return service.recentMany(symbols, limit);
    }

    // 일괄 수집: /api/fx/ingest-all?days=15
    @PostMapping("/ingest-all")
    public String ingestAll(@RequestParam(defaultValue = "15") int days) {
        service.refreshAll(days);
        return "ok";
    }

}
