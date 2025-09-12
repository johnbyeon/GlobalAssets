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
@RequestMapping("/rate/news")
public class NewsByKeyController {

    private final NewsService newsService;

    @GetMapping("/{key}")
    public String page(@PathVariable String key, Model model) {
        // key를 displayNameFor에서 한글 종목명으로 변환
        String title = newsService.displayNameFor(key);

        // 해당 key의 관련 뉴스 조회
        List<NewsItemDto> items = newsService.searchByKey(key, 10);

        model.addAttribute("title", title);
        model.addAttribute("items", items);

        // 항상 같은 html 호출
        return "news/news_list";
    }
}
