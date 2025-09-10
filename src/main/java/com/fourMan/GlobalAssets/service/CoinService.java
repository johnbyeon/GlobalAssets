package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.CandleDto;
import com.fourMan.GlobalAssets.dto.TickerDto;
import com.fourMan.GlobalAssets.upbit.UpbitClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final UpbitClient upbit;

    public static final List<String> DEFAULT_5 = List.of(
            "KRW-BTC", "KRW-ETH", "KRW-XRP", "KRW-SOL", "KRW-DOGE"
    );

    public List<TickerDto> tickers(List<String> markets) {
        return upbit.getTickersKRW(markets == null || markets.isEmpty() ? DEFAULT_5 : markets);
    }

    @Cacheable(cacheNames = "candles", key = "#market + ':' + #frame + ':' + #count")
    public List<CandleDto> candles(String market, String frame, int count) {
        return switch (frame) {
            case "1m"  -> upbit.getMinuteCandles(market, 1, count);
            case "5m"  -> upbit.getMinuteCandles(market, 5, count);
            case "15m" -> upbit.getMinuteCandles(market, 15, count);
            case "1h"  -> upbit.getMinuteCandles(market, 60, count);
            case "4h"  -> upbit.getMinuteCandles(market, 240, count);
            case "1d"  -> upbit.getDailyCandles(market, count);
            default    -> upbit.getMinuteCandles(market, 1, count);
        };
    }
}
