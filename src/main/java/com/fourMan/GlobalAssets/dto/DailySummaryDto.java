package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.DailySummary;
import com.fourMan.GlobalAssets.entity.Viewers;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailySummaryDto {

    private Long assetId;

    private Timestamp timestamp;
    //현재가격
    private Double Price;
    //전일대비 가격변화
    private Double change;
    //전일대비 등락률
    private Double changePercent;

    public static DailySummaryDto fromEntity(DailySummary entity) {
        return new DailySummaryDto(
                entity.getAssetId(),
                entity.getTimestamp(),
                entity.getPrice(),
                entity.getChange(),
                entity.getChangePercent()
        );
    }

    // DTO -> Article
    public static DailySummary fromDto(DailySummaryDto dto) {
        DailySummary entity = new DailySummary();
        entity.setAssetId(dto.getAssetId());
        entity.setTimestamp(dto.getTimestamp());
        entity.setPrice(dto.getPrice());
        entity.setChange(dto.getChange());
        entity.setChangePercent(dto.getChangePercent());
        return entity;
    }



}
