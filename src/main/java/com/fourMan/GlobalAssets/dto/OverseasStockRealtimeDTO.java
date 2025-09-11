package com.fourMan.GlobalAssets.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OverseasStockRealtimeDTO {
    private String rsym;    // 실시간종목코드
    private String symb;    // 종목코드
    private String zdiv;    // 수수점자리수
    private String tymd;    // 현지영업일자
    private String xymd;    // 현지일자
    private String xhms;    // 현지시간
    private String kymd;    // 한국일자
    private String khms;    // 한국시간
    private String open;    // 시가
    private String high;    // 고가
    private String low;     // 저가
    private String last;    // 현재가
    private String sign;    // 대비구분
    private String diff;    // 전일대비
    private String rate;    // 등락율
    private String pbid;    // 매수호가
    private String pask;    // 매도호가
    private String vbid;    // 매수잔량
    private String vask;    // 매도잔량
    private String evol;    // 체결량
    private String tvol;    // 거래량
    private String tamt;    // 거래대금
    private String bivl;    // 매도체결량
    private String asvl;    // 매수체결량
    private String strn;    // 체결강도
    private String mtyp;    // 시장구분 (1:장중, 2:장전, 3:장후)
}
