package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.NewsItemDto;
import com.fourMan.GlobalAssets.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

// com.fourMan.GlobalAssets.controller.NewsMainPageController
@Controller
@RequiredArgsConstructor
public class NewsMainPageController {

    private final NewsService newsService;

    // 메인 페이지
    @GetMapping("/news")
    public String newsMain(Model model) {
        model.addAttribute("keys", newsService.keys());
        model.addAttribute("displayNames", newsService.displayNamesMap());
        model.addAttribute("economyItems", newsService.searchEconomy(15));
        return "news/news_main"; // 템플릿 경로
    }

    // ★ 추가: /rate/news로 오면 /news로 보내기
    @GetMapping("/rate/news")
    public String redirectNews() {
        return "redirect:/news";
    }

}
