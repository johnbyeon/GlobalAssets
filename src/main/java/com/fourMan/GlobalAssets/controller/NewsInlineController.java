package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.NewsItemDto;
import com.fourMan.GlobalAssets.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/embed/news")
public class NewsInlineController {

    private final NewsService newsService;

    /** 임베드용 미니 리스트: GET /embed/news/{key} */
    @GetMapping("/{key}")
    public String page(@PathVariable String key, Model model) {
        String title = newsService.displayNameFor(key);
        List<NewsItemDto> items = newsService.searchByKey(key, 10);
        model.addAttribute("title", title);
        model.addAttribute("items", items);
        return "news/news_inline"; // 템플릿: news/news_inline.html
    }
}
