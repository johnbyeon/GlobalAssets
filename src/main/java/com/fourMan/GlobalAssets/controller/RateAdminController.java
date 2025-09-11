
package com.fourMan.GlobalAssets.controller;


import com.fourMan.GlobalAssets.client.RateTwelveDataClient;
import com.fourMan.GlobalAssets.service.RateFxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rate/admin")
public class RateAdminController {

    private final RateFxService rateFxService;
    private final RateTwelveDataClient twelve;

    // ✅ 컨트롤러에서 직접 주입받아 씀: getApiKey() 필요 없음
    @org.springframework.beans.factory.annotation.Value("${twelvedata.apikey}")
    private String twelveApiKey;

    @org.springframework.beans.factory.annotation.Value("${twelvedata.base-url}")
    private String baseUrl;


/** 수동 FX 적재 트리거 *//*

    @ResponseBody
    @GetMapping("/refresh-fx")
    public ResponseEntity<String> refreshFx(@RequestParam(defaultValue = "120") int days) {
        rateFxService.refreshFx(days);
        return ResponseEntity.ok("REFRESH FX OK (days=" + days + ")");
    }


 TwelveData 설정 확인(마스킹) */

    @ResponseBody
    @GetMapping("/twelve/ping")
    public ResponseEntity<String> twelvePing() {
        String masked = (twelveApiKey == null) ? "null"
                : twelveApiKey.substring(0, Math.min(4, twelveApiKey.length())) + "****";
        return ResponseEntity.ok("baseUrl=" + baseUrl + ", apikey=" + masked);
    }


/** 원본 JSON 그대로 보기 (심볼/사이즈/키 오버라이드 가능) */

    @ResponseBody
    @GetMapping("/twelve/raw")
    public ResponseEntity<String> twelveRaw(@RequestParam String symbol,
                                            @RequestParam(defaultValue = "5") int size,
                                            @RequestParam(name = "apikey", required = false) String apikeyOverride) {
        String key = (apikeyOverride != null && !apikeyOverride.isBlank()) ? apikeyOverride : twelveApiKey;
        String json = twelve.fetchDailySeriesRaw(symbol, size, key);
        return ResponseEntity.ok(json);
    }


/** 파싱 결과 간단 확인 */

    @ResponseBody
    @GetMapping("/twelve/series")
    public ResponseEntity<String> twelveSeries(@RequestParam String symbol,
                                               @RequestParam(defaultValue = "5") int size,
                                               @RequestParam(name = "apikey", required = false) String apikeyOverride) {
        String key = (apikeyOverride != null && !apikeyOverride.isBlank()) ? apikeyOverride : twelveApiKey;
        var bars = twelve.fetchDailySeries(symbol, size, key);
        StringBuilder sb = new StringBuilder("count=" + bars.size());
        if (!bars.isEmpty()) {
            var b = bars.get(0);
            // 수정
            sb.append(b.getDate())
                    .append(" C=").append(b.getClose())  // close만 출력
                    .append('\n');
        }
        return ResponseEntity.ok(sb.toString());
    }
}

