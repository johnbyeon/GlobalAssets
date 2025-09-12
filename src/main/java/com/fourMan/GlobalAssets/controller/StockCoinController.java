package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.AssetsDto;
import com.fourMan.GlobalAssets.service.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StockCoinController {
    private final AssetService assetService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/stockCoin")
    public String stockCoinPage(Model model) {
        try {
            List<AssetsDto> stockData = assetService.findAllByCategory("STOCK");
            List<AssetsDto> coinData = assetService.findAllByCategory("CRYPTO");
            if (stockData == null || coinData == null) {
                log.warn("stockData or coinData is null");
                return "error"; // 오류 페이지로 리다이렉션
            }
            model.addAttribute("stockData", stockData);
            model.addAttribute("coinData", coinData);
            return "stockCoin";
        } catch (Exception e) {
            log.error("Error in stockCoinPage: ", e);
            return "error"; // 오류 처리
        }
    }
}