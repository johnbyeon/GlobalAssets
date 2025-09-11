package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.config.ADMIN;
import com.fourMan.GlobalAssets.service.WebSocketClientStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 이 클래스는 /start/websocket URL로 요청을 받아서 WebSocket을 연결해.
// 중학생 설명: 이 코드는 웹사이트 주소로 들어오면 "WebSocket 연결 시작해!"라고 하는 창구야.

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebSocketController {
    public static final String KOREA_STOCK_WS_DOMAIN = "ws://ops.koreainvestment.com:21000";
    public static final String KOREA_STOCK_WS_URL = "/tryitout/";

    private final WebSocketClientStockService stockService;


    @GetMapping("/start/websocket")
    public String startWebSocket() {
        log.info("Received request to start WebSocket at /start/websocket");
        try {
            // 세션 1개만 연결 (국내 전용)
            stockService.connect(KOREA_STOCK_WS_DOMAIN, KOREA_STOCK_WS_URL+ADMIN.INIT_STOCK.KR_TRID);

            return "국내주식 WebSocket 연결 및 구독 시작!";
        } catch (Exception e) {
            log.error("국내주식 WebSocket 연결 실패: {}", e.getMessage());
            return "국내주식 WebSocket 연결 실패: " + e.getMessage();
        }
    }

    @GetMapping("/stop/websocket")
    public String stopWebSocket() {
        log.info("Received request to stop WebSocket at /stop/websocket");
        try {
            stockService.disconnect(); // WebSocket 연결 종료
            return "WebSocket 연결이 종료되었어요.";
        } catch (Exception e) {
            log.error("WebSocket 종료 실패: {}", e.getMessage());
            return "WebSocket 종료 실패: " + e.getMessage();
        }
    }
}