package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.DailySummaryDto;
import com.fourMan.GlobalAssets.dto.NewsItemDto;
import com.fourMan.GlobalAssets.service.ApprovalKeyService;
import com.fourMan.GlobalAssets.service.NewsService;
import com.fourMan.GlobalAssets.service.RateFxService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ApprovalKeyService approvalKeyService;
    private final NewsService newsService;
    private final RateFxService rateFxService;

    /**
     * 루트(/) 접속 시 → index.html 로드 + 뉴스 리스트 전달
     */
    @GetMapping({"/",""})
    public String dashboard(Model model) {
        List<NewsItemDto> newsList = newsService.searchEconomy(15);
        model.addAttribute("newsList", newsList);

        // 환율

        // ✅ 환율 최근 10일 (USD/KRW)
        List<DailySummaryDto> usdRates = rateFxService.recentOne("USD/KRW", 10);
        model.addAttribute("usdRates", usdRates);
        return "index"; // templates/index.html
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/exchange")
    public String userOnlyExchange() {
        return "exchange";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/admin")
    public String adminOnlyAdminPage() {
        return "admin";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/adminOnly")
    public String adminOnly() {
        return "관리자 전용 페이지!";
    }
}
