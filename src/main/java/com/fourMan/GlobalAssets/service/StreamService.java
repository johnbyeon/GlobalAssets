package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.CandleDto;
import com.fourMan.GlobalAssets.dto.TickerDto;
import com.fourMan.GlobalAssets.upbit.UpbitRest;
import com.fourMan.GlobalAssets.upbit.UpbitWsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreamService {

    private final UpbitWsClient ws;
    private final CandleAggregator agg;
    private final UpbitRest rest;
    private final CandleStoreService store; // ★ 추가

    public Flux<TickerDto> tickerStream(String market) {
        return ws.subscribeTickerJson(market).map(s -> {
            double price = extractDouble(s, "\"trade_price\":");
            long ts = (long) extractDouble(s, "\"timestamp\":");
            return TickerDto.builder().market(market).price(price).ts(ts).source("UPBIT").build();
        });
    }

    public Flux<CandleDto> candleStream(String market, String frame) {
        // upstream 연결: 틱 수신 → 집계기 onTick (멀티구독 시에도 멱등)
        ws.subscribeTickerJson(market).subscribe(s -> {
            double price = extractDouble(s, "\"trade_price\":");
            long ts = (long) extractDouble(s, "\"timestamp\":");
            agg.onTick(market, frame, ts, price);
        });
        return agg.stream(market, frame);
    }

    // 초기 캔들: DB 우선 → 없으면 REST로 채우고 DB 저장
    public List<CandleDto> initialCandles(String market, String frame, int count) {
        if (store.hasAny(market, frame)) {
            var got = store.latest(market, frame, count);
            if (!got.isEmpty()) return got;
        }
        var fromApi = rest.candles(market, frame, count);
        if (!fromApi.isEmpty()) store.upsertAll(fromApi);
        return fromApi;
    }

    private static double extractDouble(String json, String key) {
        int i = json.indexOf(key);
        if (i < 0) return 0d;
        int s = i + key.length(), e = s;
        while (e < json.length() && "0123456789.-".indexOf(json.charAt(e)) >= 0) e++;
        return Double.parseDouble(json.substring(s, e));
    }
}
