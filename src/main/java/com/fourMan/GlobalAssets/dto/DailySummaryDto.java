// src/main/java/com/fourMan/GlobalAssets/dto/DailySummaryDto.java
package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailySummaryDto {

    private Long assetId;
    private Timestamp timestamp;   // 일자(자정) 기준 타임스탬프
    private Double price;          // 현재가
    private Double priceChange;    // 전일대비 금액
    private Double changePercent;  // 전일대비 등락률

    public static DailySummaryDto fromEntity(DailySummaryEntity entity) {
        return new DailySummaryDto(
                entity.getAssetId(),
                entity.getTimestamp(),
                entity.getPrice(),
                entity.getPriceChange(),
                entity.getChangePercent()
        );
    }

    public static DailySummaryEntity fromDto(DailySummaryDto dto) {
        DailySummaryEntity entity = new DailySummaryEntity();
        entity.setAssetId(dto.getAssetId());
        entity.setTimestamp(dto.getTimestamp());
        entity.setPrice(dto.getPrice());
        entity.setPriceChange(dto.getPriceChange());
        entity.setChangePercent(dto.getChangePercent());
        return entity;
    }

    /* ===== 화면(Thymeleaf)용 파생 게터 ===== */

    /** 템플릿에서 ${r.date} 로 쓰는 값 (LocalDate) */
    public LocalDate getDate() {
        return timestamp == null ? null : timestamp.toLocalDateTime().toLocalDate();
    }

    /** 템플릿의 ${r.change} 호환 (priceChange 별칭) */
    public Double getChange() {
        return priceChange;
    }

    /** 상승/하락 아이콘용 */
    public boolean isUp()   { return priceChange != null && priceChange > 0; }
    public boolean isDown() { return priceChange != null && priceChange < 0; }
}
