package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class DailySummaryEntity {
    //종목 번호
    @Id
    private Long assetId;
    //종목 기준날짜
    private Timestamp timestamp;
    //현재가격
    private Double price;
    //전일대비 가격변화
    private Double priceChange;
    //전일대비 등락률
    private Double changePercent;
}
