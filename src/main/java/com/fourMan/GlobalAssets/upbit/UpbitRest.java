package com.fourMan.GlobalAssets.upbit;

import com.fourMan.GlobalAssets.dto.CandleDto;
import com.fourMan.GlobalAssets.dto.TickerDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpbitRest {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.upbit.com")
            .build();

    // frame: 1m/5m/15m/1h/4h/1d
    public List<CandleDto> candles(String market, String frame, int count) {
        String path = switch (frame) {
            case "1m" -> "/v1/candles/minutes/1";
            case "5m" -> "/v1/candles/minutes/5";
            case "15m" -> "/v1/candles/minutes/15";
            case "1h" -> "/v1/candles/minutes/60";
            case "4h" -> "/v1/candles/minutes/240";
            case "1d" -> "/v1/candles/days";
            default -> "/v1/candles/minutes/1";
        };

        List<Map> arr = webClient.get()
                .uri(u -> u.path(path)
                        .queryParam("market", market)
                        .queryParam("count", Math.min(count, 200))
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();

        if (arr == null) return List.of();

        List<CandleDto> out = new ArrayList<>();
        for (Map<String,Object> m : arr) {
            out.add(CandleDto.builder()
                    .market(market).frame(frame)
                    .t(((Number)m.getOrDefault("timestamp", 0)).longValue())
                    .o(asD(m.get("opening_price")))
                    .h(asD(m.get("high_price")))
                    .l(asD(m.get("low_price")))
                    .c(asD(m.get("trade_price")))
                    .v(asD(m.get("candle_acc_trade_volume")))
                    .build());
        }
        // 업비트는 최신→과거, 차트는 과거→최신 순이 편함
        Collections.reverse(out);
        return out;
    }

    public List<TickerDto> tickers(List<String> markets) {
        if (markets == null || markets.isEmpty()) return List.of();
        String q = markets.stream().collect(Collectors.joining(","));
        List<Map> arr = webClient.get()
                .uri(u -> u.path("/v1/ticker").queryParam("markets", q).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();

        if (arr == null) return List.of();

        List<TickerDto> out = new ArrayList<>();
        for (Map<String,Object> m : arr) {
            out.add(TickerDto.builder()
                    .market(String.valueOf(m.get("market")))
                    .price(asD(m.get("trade_price")))
                    .ts(((Number)m.getOrDefault("timestamp", 0)).longValue())
                    .prevClose(asD(m.get("prev_closing_price")))
                    .change(asD(m.get("signed_change_price")))
                    .changeRate(asD(m.get("signed_change_rate")))
                    .build());
        }
        return out;
    }

    private static double asD(Object o) {
        if (o == null) return 0.0;
        return Double.parseDouble(String.valueOf(o));
    }
}
