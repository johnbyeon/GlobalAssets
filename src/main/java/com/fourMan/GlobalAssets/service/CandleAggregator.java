package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.CandleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class CandleAggregator {

    private final CandleStoreService store; // DB upsert 서비스 (네 프로젝트 클래스명 맞춰둠)

    private final Map<String, CandleDto> last = new ConcurrentHashMap<>();
    private final Map<String, Sinks.Many<CandleDto>> sinks = new ConcurrentHashMap<>();

    /** 프레임 문자열 → 분 */
    public static long minutesOf(String frame) {
        return switch (frame) {
            case "1m" -> 1;
            case "5m" -> 5;
            case "15m" -> 15;
            case "1h" -> 60;
            case "4h" -> 240;
            case "1d" -> 1440;
            default -> 1;
        };
    }

    /** 슬롯 기준(ms). 일봉은 아예 00:00:00 Asia/Seoul로 고정 */
    public static long slotOf(long ts, String frame) {
        if ("1d".equals(frame)) {
            var cal = java.util.Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul")); // ★ 타임존 명시
            cal.setTimeInMillis(ts);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            return cal.getTimeInMillis();
        }
        long unitMs = minutesOf(frame) * 60_000;
        return (ts / unitMs) * unitMs;
    }

    /** 실시간 구독 스트림 (백프레셔 완충 + 샘플링) */
    public Flux<CandleDto> stream(String market, String frame) {
        String key = keyOf(market, frame);
        var sink = getOrCreateSink(key); // ★ 안전하게 생성
        return sink.asFlux().sample(Duration.ofMillis(200));
    }

    /** 틱 반영 */
    public void onTick(String market, String frame, long ts, double price) {
        String key = keyOf(market, frame);
        long slot = slotOf(ts, frame);
        CandleDto cur = last.get(key);

        // 한국어 틱 로그
        log.info("틱 수신 ▶ 마켓={} | 주기={} | 체결시각(ms)={} | 슬롯(ms)={} | 가격={}",
                market, frame, ts, slot, price);

        // 새 슬롯(봉 교체)인지 확인
        if (cur == null || cur.getT() != slot) {
            // 이전 봉 저장
            if (cur != null) {
                store.upsert(cur);
                log.info("봉 마감 ▶ 마켓={} | 주기={} | 슬롯(ms)={} | 시가={} | 고가={} | 저가={} | 종가={} | 거래량={}",
                        cur.getMarket(), cur.getFrame(), cur.getT(),
                        cur.getO(), cur.getH(), cur.getL(), cur.getC(), cur.getV());
            }

            // 새 봉 시작
            CandleDto n = CandleDto.builder()
                    .market(market).frame(frame).t(slot)
                    .o(price).h(price).l(price).c(price).v(0.0)
                    .build();

            last.put(key, n);
            getOrCreateSink(key).tryEmitNext(n); // ★ NPE 방지용 getOrCreateSink 사용
            store.upsert(n); // 현재 봉도 저장 (선호 로직 유지)

            log.info("새 봉 시작 ▶ 마켓={} | 주기={} | 슬롯(ms)={} | 시작가={}",
                    n.getMarket(), n.getFrame(), n.getT(), n.getO());
            return;
        }

        // 같은 슬롯이면 봉 갱신
        cur.setH(Math.max(cur.getH(), price));
        cur.setL(Math.min(cur.getL(), price));
        cur.setC(price);
        // cur.setV(cur.getV() + ???);  // 거래량 반영이 필요하면 여기에

        getOrCreateSink(key).tryEmitNext(cur);
        store.upsert(cur); // upsert 전략 유지 (원하면 배치/주기 저장으로 변경 가능)

        log.debug("봉 갱신 ▶ 마켓={} | 주기={} | 슬롯(ms)={} | 현재가={} | H/L={}/{}",
                market, frame, slot, price, cur.getH(), cur.getL());
    }

    /* ================== 내부 유틸 ================== */

    private String keyOf(String market, String frame) {
        return market + "|" + frame;
    }

    /** sink가 없을 수 있으므로 항상 안전 생성 */
    private Sinks.Many<CandleDto> getOrCreateSink(String key) {
        return sinks.computeIfAbsent(key, k -> Sinks.many().multicast().onBackpressureBuffer());
    }
}
