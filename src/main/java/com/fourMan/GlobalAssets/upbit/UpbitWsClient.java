package com.fourMan.GlobalAssets.upbit;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UpbitWsClient {

    private static final String UPBIT_WS_URL = "wss://api.upbit.com/websocket/v1";

    // 마켓별 공유 스트림 캐시
    private final ConcurrentHashMap<String, Flux<String>> marketStreamCache = new ConcurrentHashMap<>();

    public Flux<String> subscribeTickerJson(String market) {
        return marketStreamCache.computeIfAbsent(market, this::buildSharedTickerStream);
    }

    private Flux<String> buildSharedTickerStream(String market) {
        return Flux.defer(() -> connectOnce(market))
                .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(2))
                        .maxBackoff(Duration.ofSeconds(10)))
                .publish()
                .refCount(1);
    }

    // Flux<String>을 반환하면서 내부 execute에 error 콜백 지정
    private Flux<String> connectOnce(String market) {
        ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

        return Flux.create(sink -> {
            client.execute(URI.create(UPBIT_WS_URL), session -> {
                String sub = String.format(
                        "[{\"ticket\":\"%s\"},{\"type\":\"ticker\",\"codes\":[\"%s\"]}]",
                        "ws-" + market, market
                );

                // 1) 구독 메시지 보내기
                Mono<Void> send = session.send(Mono.just(session.textMessage(sub)));

                // 2) 메시지 수신 → sink.next()
                Flux<String> receive = session.receive()
                        .map(UpbitWsClient::decodeMessage)
                        .doOnNext(sink::next)
                        .doOnError(sink::error)
                        .doOnComplete(sink::complete);

                return send.thenMany(receive).then();
            }).subscribe(
                    null,              // onNext (Mono<Void>라 필요 없음)
                    sink::error,       // onError → sink로 전달
                    sink::complete     // onComplete → sink로 완료 알림
            );
        });
    }

    private static String decodeMessage(WebSocketMessage msg) {

        return msg.getPayloadAsText(StandardCharsets.UTF_8);

    }

}

