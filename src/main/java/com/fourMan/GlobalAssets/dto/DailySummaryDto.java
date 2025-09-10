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

    // DTO -> Article
    public static DailySummaryEntity fromDto(DailySummaryDto dto) {
        DailySummaryEntity entity = new DailySummaryEntity();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());
        entity.setPrevClose(dto.getPrevClose());
        entity.setDelta(dto.getDelta());
        entity.setDeltaPercent(dto.getDeltaPercent());
        return entity;
    }



}
