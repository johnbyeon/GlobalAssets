package com.fourMan.GlobalAssets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

// 이 클래스는 WebSocket에 구독 요청을 보낼 데이터를 정리해.
// 중학생 설명: 실시간 데이터 받으려면 서버에 "이 종목 주가 알려줘!"라고 요청하는 편지 내용이야. JSON으로 만들어서 보내.

@Data
@NoArgsConstructor
public class WebSocketSubscribeRequestDto {
    public static final String CUST_TYPE_USER = "P";
    public static final String TR_TPYE_SUBSCRIBE = "1";
    public static final String CONTENT_TYPE_UTF_8 = "utf-8";
    @JsonProperty("header")
    private Header header; // 헤더 부분

    @JsonProperty("body")
    private Body body; // 바디 부분

    @Data
    public static class Header {
        @JsonProperty("approval_key")
        private String approvalKey; // approval_key 넣음

        @JsonProperty("custtype")
        private String custtype = CUST_TYPE_USER; // 개인(P)으로 고정

        @JsonProperty("tr_type")
        private String trType = TR_TPYE_SUBSCRIBE; // 구독 시작(1)

        @JsonProperty("content-type")
        private String contentType = CONTENT_TYPE_UTF_8; // UTF-8 인코딩
    }

    @Data
    public static class Body {
        @JsonProperty("input")
        private Input input; // 입력 데이터

        @Data
        public static class Input {
            @JsonProperty("tr_id")
            private String trId; // "H0STCNT0"으로 설정 (현재가 데이터)

            @JsonProperty("tr_key")
            private String trKey; // 종목 코드, 예: "005930"
        }
    }
}