// src/main/java/com/fourMan/GlobalAssets/dto/DailySummaryDto.java
package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailySummaryDto {

    private Long id;

    private LocalDate date;
    //현재가격
    private BigDecimal price;
    //전일가격
    private BigDecimal prevClose;
    //전일대비 가격변화
    private BigDecimal delta;
    //전일대비 등락률
    private BigDecimal deltaPercent;

    public static DailySummaryDto fromEntity(DailySummaryEntity entity) {
        return new DailySummaryDto(
                entity.getId(),
                entity.getDate(),
                entity.getPrice(),
                entity.getPrevClose(),
                entity.getDelta(),
                entity.getDeltaPercent()
        );
    }

    public static DailySummaryEntity fromDto(DailySummaryDto dto) {
        DailySummaryEntity entity = DailySummaryEntity.builder()
                .id(dto.getId())
                .date(dto.getDate())
                .price(dto.getPrice())
                .prevClose(dto.getPrevClose())
                .delta(dto.getDelta())
                .deltaPercent(dto.getDeltaPercent())
                .build();
        return entity;
    }

//    /* ===== 화면(Thymeleaf)용 파생 게터 ===== */
//
//    /** 템플릿에서 ${r.date} 로 쓰는 값 (LocalDate) */
//    public LocalDate getDate() {
//        return timestamp == null ? null : timestamp.toLocalDateTime().toLocalDate();
//    }
//
//    /** 템플릿의 ${r.change} 호환 (priceChange 별칭) */
//    public Double getChange() {
//        return priceChange;
//    }
//
//    /** 상승/하락 아이콘용 */
//    public boolean isUp()   { return priceChange != null && priceChange > 0; }
//    public boolean isDown() { return priceChange != null && priceChange < 0; }
}
