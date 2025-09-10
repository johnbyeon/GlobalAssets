package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.CandleDto;
import com.fourMan.GlobalAssets.dto.TickerDto;
import com.fourMan.GlobalAssets.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StreamController {
    private final StreamService svc;

    @GetMapping(value="/stream/ticker/{market}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TickerDto> ticker(@PathVariable String market) {
        return svc.tickerStream(market).sample(Duration.ofMillis(100));
    }

    @GetMapping(value="/stream/candle/{market}/{frame}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CandleDto> candle(@PathVariable String market, @PathVariable String frame) {
        return svc.candleStream(market, frame);
    }

    @GetMapping("/candles")
    public List<CandleDto> initial(@RequestParam String market,
                                   @RequestParam String frame,
                                   @RequestParam(defaultValue = "200") int count) {
        return svc.initialCandles(market, frame, Math.min(count, 200));
    }
}
