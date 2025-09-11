package com.fourMan.GlobalAssets.dto;

import lombok.Data;

// 이 클래스는 WebSocket에서 받은 실시간 주식 데이터를 정리해.
// 중학생 설명: 서버가 보내준 주가 데이터(문자열)를 나누고 저장하는 상자야. 데이터가 "종목코드^현재가^시간" 이런 식으로 와서 파싱해.

@Data
public class WebSocketStockResponseDto {
    private String mkscShrnIscd;    // 유가증권 단축 종목코드
    private String stckCntgHour;    // 주식 체결 시간
    private String stckPrpr;        // 주식 현재가
    private String prdyVrssSign;    // 전일 대비 부호
    private String prdyVrss;        // 전일 대비
    private String prdyCtrt;        // 전일 대비율
    private String wghnAvrgStckPrc; // 가중 평균 주식 가격
    private String stckOprc;        // 주식 시가
    private String stckHgpr;        // 주식 최고가
    private String stckLwpr;        // 주식 최저가
    private String askp1;           // 매도호가1
    private String bidp1;           // 매수호가1
    private String cntgVol;         // 체결 거래량
    private String acmlVol;         // 누적 거래량
    private String acmlTrPbmn;      // 누적 거래 대금
    private String selnCntgCsnu;    // 매도 체결 건수
    private String shnuCntgCsnu;    // 매수 체결 건수
    private String ntbyCntgCsnu;    // 순매수 체결 건수
    private String cttr;            // 체결강도
    private String selnCntgSmtn;    // 총 매도 수량
    private String shnuCntgSmtn;    // 총 매수 수량
    private String ccldDvsn;        // 체결구분
    private String shnuRate;        // 매수비율
    private String prdyVolVrssAcmlVolRate; // 전일 거래량 대비 등락율
    private String oprcHour;        // 시가 시간
    private String oprcVrssPrprSign; // 시가대비구분
    private String oprcVrssPrpr;    // 시가대비
    private String hgprHour;        // 최고가 시간
    private String hgprVrssPrprSign; // 고가대비구분
    private String hgprVrssPrpr;    // 고가대비
    private String lwprHour;        // 최저가 시간
    private String lwprVrssPrprSign; // 저가대비구분
    private String lwprVrssPrpr;    // 저가대비
    private String bsopDate;        // 영업 일자
    private String newMkopClsCode;  // 신 장운영 구분 코드
    private String trhtYn;          // 거래정지 여부
    private String askpRsqn1;       // 매도호가 잔량1
    private String bidpRsqn1;       // 매수호가 잔량1
    private String totalAskpRsqn;   // 총 매도호가 잔량
    private String totalBidpRsqn;   // 총 매수호가 잔량
    private String volTnrt;         // 거래량 회전율
    private String prdySmnsHourAcmlVol; // 전일 동시간 누적 거래량
    private String prdySmnsHourAcmlVolRate; // 전일 동시간 누적 거래량 비율
    private String hourClsCode;     // 시간 구분 코드
    private String mrktTrtmClsCode; // 임의종료구분코드
    private String viStndPrc;       // 정적VI발동기준가
}