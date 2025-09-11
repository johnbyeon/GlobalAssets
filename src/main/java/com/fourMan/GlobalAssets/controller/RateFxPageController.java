package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.DailySummaryDto;
import com.fourMan.GlobalAssets.service.RateFxService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rate/fx")
public class RateFxPageController {

    private final RateFxService service;

    // 단일 보기(예전 페이지) - 유지
    @GetMapping("/daily")
    public String page(@RequestParam(defaultValue = "USD/KRW") String symbol,
                       @RequestParam(defaultValue = "5") int limit,
                       Model model) {
        List<DailySummaryDto> rows = service.recentOne(symbol, limit);
        model.addAttribute("symbol", symbol);
        model.addAttribute("rows", rows);
        return "rate/rate_fx_daily";
    }

    // 보드 보기: 원달러, 원엔(100), 원위안, 원유로, 원파운드
    @GetMapping("/board")
    public String board(@RequestParam(defaultValue = "10") int limit, Model model) {
        List<DailySummaryDto> usd = service.recentOne("USD/KRW", limit);
        List<DailySummaryDto> jpy = service.recentOne("JPY/KRW", limit); // 100엔 기준 스케일링
        List<DailySummaryDto> cny = service.recentOne("CNY/KRW", limit);
        List<DailySummaryDto> eur = service.recentOne("EUR/KRW", limit);
        List<DailySummaryDto> gbp = service.recentOne("GBP/KRW", limit);

        model.addAttribute("usdRows", toViewRows(usd, false));
        model.addAttribute("jpyRows", toViewRows(jpy, true));     // JPY만 100엔 기준으로 *100
        model.addAttribute("cnyRows", toViewRows(cny, false));
        model.addAttribute("eurRows", toViewRows(eur, false));
        model.addAttribute("gbpRows", toViewRows(gbp, false));

        return "rate/rate_fx_board";
    }

    private List<RowView> toViewRows(List<DailySummaryDto> src, boolean jpyHundred) {
        return src.stream().map(d -> {
            LocalDate date = d.getDate();
            BigDecimal price = nvl(d.getPrice());
            BigDecimal chg = nvl(d.getPriceChange());
            BigDecimal pct = nvl(d.getChangePercent());

            if (jpyHundred) { // 100엔 기준
                price = price.multiply(BigDecimal.valueOf(100));
                chg = chg.multiply(BigDecimal.valueOf(100));
            }

            boolean up = chg.compareTo(BigDecimal.ZERO) > 0;
            boolean down = chg.compareTo(BigDecimal.ZERO) < 0;

            String priceText = fmt2(price);
            String changeText = fmt2(chg);
            String changePctText = fmt2(pct) + "%";

            return new RowView(date, priceText, changeText, changePctText, up, down);
        }).toList();
    }

    private static BigDecimal nvl(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
    private static String fmt2(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    @Getter
    public static class RowView {
        private final LocalDate date;
        private final String priceText;
        private final String changeText;
        private final String changePercentText;
        private final boolean up;
        private final boolean down;
        public RowView(LocalDate date, String priceText, String changeText, String changePercentText, boolean up, boolean down) {
            this.date = date;
            this.priceText = priceText;
            this.changeText = changeText;
            this.changePercentText = changePercentText;
            this.up = up;
            this.down = down;
        }
    }
}
