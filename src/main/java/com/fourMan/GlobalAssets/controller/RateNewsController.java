package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.RateNewsItem;
import com.fourMan.GlobalAssets.service.RateNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RateNewsController {

    private final RateNewsService newsService;

    // 메인 경제 뉴스 페이지
    @GetMapping("/rate/rate_news_main")
    public String newsEconomyPage(@RequestParam(defaultValue = "15") int limit, Model model) {
        List<RateNewsItem> items = newsService.searchEconomy(limit);
        sortAndRenumber(items);
        model.addAttribute("title", "ECONOMY");
        model.addAttribute("items", items);
        return "rate/rate_news_main";
    }

    // 특정 자산(차트 옆): asset_id로 뉴스 10개
    @GetMapping("/rate/news/by-asset/{assetId}")
    public String newsByAssetPage(@PathVariable long assetId,
                                  @RequestParam(defaultValue = "10") int limit,
                                  Model model) {
        List<RateNewsItem> items = newsService.searchByAssetId(assetId, limit);
        sortAndRenumber(items);
        model.addAttribute("title", "ASSET#" + assetId);
        model.addAttribute("items", items);
        return "rate/rate_news_main";
    }

    // JSON API
    @ResponseBody
    @GetMapping(value = "/rate/api/news/by-asset/{assetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RateNewsItem> apiNewsByAsset(@PathVariable long assetId,
                                             @RequestParam(defaultValue = "10") int limit) {
        return newsService.searchByAssetId(assetId, limit);
    }

    // 정렬/번호 재부여 (빈 번호가 뒤로 가도록)
    private void sortAndRenumber(List<RateNewsItem> items) {
        if (items == null) return;
        items.sort(Comparator.comparingInt(it -> it.getNo() == 0 ? Integer.MAX_VALUE : it.getNo()));
        for (int i = 0; i < items.size(); i++) items.get(i).setNo(i + 1);
    }
}
