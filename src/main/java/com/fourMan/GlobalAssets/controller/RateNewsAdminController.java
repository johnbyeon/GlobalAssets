package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.RateNewsItem;
import com.fourMan.GlobalAssets.service.RateNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rate/admin/news")
public class RateNewsAdminController {

    private final RateNewsService newsService;

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RateNewsItem> search(@RequestParam String q,
                                     @RequestParam(defaultValue = "10") int limit) {
        return newsService.searchForChart(q, limit);
    }
}
