package com.fourMan.GlobalAssets.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // 복합키 비교할 때 equals/hashCode 자동 생성

// JPA에서 @IdClass 사용 시 반드시 Serializable 구현 필요
public class DailySummaryKey implements Serializable {

    // 어떤 자산(코인/주식/지수 등)의 고유 ID
    // DailySummaryEntity.assetId와 매핑됨
    private Long assetId;

    // 기준 날짜(하루 단위)
    // DailySummaryEntity.timestamp와 매핑됨
    private LocalDate timestamp;
}
