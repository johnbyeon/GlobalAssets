package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.CandleDto;
import com.fourMan.GlobalAssets.dto.TickerDto;
import com.fourMan.GlobalAssets.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/krw")
public class CoinController {

    public final CoinService svc;

    // 예) /api/krw/tickers (기본 5종)
    // 또는 /api/krw/tickers?markets=KRW-BTC,KRW-ETH
    @GetMapping("/tickers")
    public List<TickerDto> tickers(@RequestParam(required = false) String markets) {
        List<String> list = (markets == null || markets.isBlank())
                ? null
                : Arrays.stream(markets.split(",")).map(String::trim).toList();
        return svc.tickers(list);
    }

    // 예) /api/krw/candles?market=KRW-BTC&frame=1m&count=200
    @GetMapping("/candles")
    public List<CandleDto> candles(@RequestParam String market,
                                   @RequestParam(defaultValue = "1m") String frame,
                                   @RequestParam(defaultValue = "200") int count) {
        return svc.candles(market, frame, Math.min(count, 200));
    }


}
