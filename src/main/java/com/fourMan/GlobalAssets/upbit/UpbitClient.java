package com.fourMan.GlobalAssets.upbit;

import com.fourMan.GlobalAssets.dto.CandleDto;
import com.fourMan.GlobalAssets.dto.TickerDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class UpbitClient {
    private final RestClient rest = RestClient.create();

    // 여러 종목 현재가 (KRW-BTC,KRW-ETH,...)
    public List<TickerDto> getTickersKRW(List<String> markets) {
        if (markets == null || markets.isEmpty()) return List.of();
        String joined = String.join(",", markets);
        List<Map<String, Object>> arr = rest.get()
                .uri("https://api.upbit.com/v1/ticker?markets={m}", joined)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().body(List.class);

        List<TickerDto> out = new ArrayList<>();
        for (Map<String, Object> o : arr) {
            out.add(TickerDto.builder()
                    .market(String.valueOf(o.get("market")))
                    .price(((Number)o.get("trade_price")).doubleValue())
                    .ts(((Number)o.get("timestamp")).longValue())
                    .source("UPBIT")
                    .build());
        }
        return out;
    }

    // 분봉: unit = 1, 3, 5, 15, 30, 60, 240
    public List<CandleDto> getMinuteCandles(String market, int unit, int count) {
        List<Map<String, Object>> arr = rest.get()
                .uri("https://api.upbit.com/v1/candles/minutes/{unit}?market={m}&count={c}",
                        unit, market, Math.min(count, 200))
                .retrieve().body(List.class);

        Collections.reverse(arr); // 최신→과거 → 과거→최신
        List<CandleDto> out = new ArrayList<>();
        for (Map<String, Object> k : arr) {
            out.add(CandleDto.builder()
                    .t(((Number)k.get("timestamp")).longValue())
                    .o(Double.parseDouble(String.valueOf(k.get("opening_price"))))
                    .h(Double.parseDouble(String.valueOf(k.get("high_price"))))
                    .l(Double.parseDouble(String.valueOf(k.get("low_price"))))
                    .c(Double.parseDouble(String.valueOf(k.get("trade_price"))))
                    .v(Double.parseDouble(String.valueOf(k.get("candle_acc_trade_volume"))))
                    .build());
        }
        return out;
    }

    // 일봉
    public List<CandleDto> getDailyCandles(String market, int count) {
        List<Map<String, Object>> arr = rest.get()
                .uri("https://api.upbit.com/v1/candles/days?market={m}&count={c}",
                        market, Math.min(count, 200))
                .retrieve().body(List.class);

        Collections.reverse(arr);
        List<CandleDto> out = new ArrayList<>();
        for (Map<String, Object> k : arr) {
            out.add(CandleDto.builder()
                    .t(((Number)k.get("timestamp")).longValue())
                    .o(Double.parseDouble(String.valueOf(k.get("opening_price"))))
                    .h(Double.parseDouble(String.valueOf(k.get("high_price"))))
                    .l(Double.parseDouble(String.valueOf(k.get("low_price"))))
                    .c(Double.parseDouble(String.valueOf(k.get("trade_price"))))
                    .v(Double.parseDouble(String.valueOf(k.get("candle_acc_trade_volume"))))
                    .build());
        }
        return out;
    }
}
