package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.NewsItemDto;
import com.fourMan.GlobalAssets.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NewsMainPageController {

    private final NewsService newsService;

    /** 메인 뉴스 페이지: GET /news */
    @GetMapping("/news")
    public String newsMain(Model model) {
        List<String> keys = newsService.keys();
        Map<String, String> displayNames = newsService.displayNamesMap();
        List<NewsItemDto> economyItems = newsService.searchEconomy(15);

        model.addAttribute("keys", keys);
        model.addAttribute("displayNames", displayNames);
        model.addAttribute("economyItems", economyItems);
        return "news/news_main";  // 템플릿: src/main/resources/templates/news/news_main.html
    }
}
