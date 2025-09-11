package com.fourMan.GlobalAssets.dto;

import lombok.Data;

@Data
public class WebSocketKRXIndexResponseDto {
    private String bstpClsCode; //업종 구분 코드
    private String bsopHour;	 //영업 시간
    private String prprNmix; //현재가 지수
    private String prdyVrssSign; //전일 대비 부호
    private String bstpNmixPrdyVrss; //업종 지수 전일 대비
    private String acmlVol; //누적 거래량
    private String acmlTrPbmn; //누적 거래 대금
    private String pcasVol;	//건별 거래량
    private String pcasTrPbmn;;	//건별 거래 대금
    private String prdyCtrt;;	//전일 대비율
    private String oprcNmix;;	//시가 지수
    private String nmixHgpr;;	//지수 최고가
    private String nmixLwpr;;	//지수 최저가
    private String oprcVrssNmixPrpr;	//시가 대비 지수 현재가
    private String oprcVrssNmixSign;	//시가 대비 지수 부호
    private String hgprVrssNmixPrpr;	//최고가 대비 지수 현재가
    private String hgprVrssNmixSign;	//최고가 대비 지수 부호
    private String lwprVrssNmixPrpr;	//최저가 대비 지수 현재가
    private String lwprVrssNmixSign;	//최저가 대비 지수 부호
    private String prdyClprVrssOprcRate;	//전일 종가 대비 시가2 비율
    private String prdyClprVrssHgprRate;	//전일 종가 대비 최고가 비율
    private String prdyClprVrssLwprRate;	//전일 종가 대비 최저가 비율
    private String uplmIssuCnt;	//상한 종목 수
    private String ascnIssuCnt;	//상승 종목 수
    private String stnrIssuCnt;	//보합 종목 수
    private String downIssuCnt;	//하락 종목 수
    private String lslmIssuCnt;	//하한 종목 수
    private String qtqtAscnIssuCnt;	//기세 상승 종목수
    private String qtqtDownIssuCnt;	//기세 하락 종목수
    private String tickVrss;	//TICK대비
}
