package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.AssetsDto;
import com.fourMan.GlobalAssets.service.RateNewsKey;
import com.fourMan.GlobalAssets.service.RateNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RateNewsController {

    private final RateNewsService newsService;

    // ★ 신규: 자산 + 뉴스 번들
    @GetMapping(value = "/rate/api/assets/{assetId}/news", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AssetsDto assetWithNews(@PathVariable Long assetId,
                                   @RequestParam(defaultValue = "15") int limit) {
        return newsService.assetWithNews(assetId, limit);
    }

    // (유지) 심볼/코드로도 번들 제공
    @GetMapping(value = "/rate/api/assets/by", produces = MediaType.APPLICATION_JSON_VALUE, params = {"q"})
    @ResponseBody
    public AssetsDto assetWithNewsByQuery(@RequestParam("q") String symbolOrCode,
                                          @RequestParam(defaultValue = "15") int limit) {
        return newsService.assetWithNewsBySymbolOrCode(symbolOrCode, limit);
    }

    // (기존 유지) 필요 시 사용중인 API들
    @GetMapping(value = "/rate/api/news/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RateNewsItem> newsByKeyApi(@PathVariable RateNewsKey key,
                                           @RequestParam(defaultValue = "15") int limit) {
        // 프리셋 섹션은 자산 독립적 → 기존 형태 유지
        return newsService.searchPreset(key, limit);
    }

    @GetMapping(value = "/rate/api/news", produces = MediaType.APPLICATION_JSON_VALUE, params = "symbol")
    @ResponseBody
    public AssetsDto newsBySymbolBundle(@RequestParam String symbol,
                                        @RequestParam(defaultValue = "15") int limit) {
        // 과거에는 List<RateNewsItem> 반환이었을 수 있으나,
        // 이제는 번들을 원하는 경우 이 엔드포인트를 사용
        return newsService.assetWithNewsBySymbolOrCode(symbol, limit);
    }

    @GetMapping(value = "/rate/api/news/economy", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RateNewsItem> apiNewsEconomy(@RequestParam(defaultValue = "15") int limit) {
        return newsService.searchEconomy(limit);
    }
}
