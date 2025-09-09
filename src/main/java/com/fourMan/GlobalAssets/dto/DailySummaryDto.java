package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
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
    private Double price;
    //전일대비 가격변화
    private Double priceChange;
    //전일대비 등락률
    private Double changePercent;

    public static DailySummaryDto fromEntity(DailySummaryEntity entity) {
        return new DailySummaryDto(
                entity.getAssetId(),
                entity.getTimestamp(),
                entity.getPrice(),
                entity.getPriceChange(),
                entity.getChangePercent()
        );
    }

    // DTO -> Article
    public static DailySummaryEntity fromDto(DailySummaryDto dto) {
        DailySummaryEntity entity = new DailySummaryEntity();
        entity.setAssetId(dto.getAssetId());
        entity.setTimestamp(dto.getTimestamp());
        entity.setPrice(dto.getPrice());
        entity.setPriceChange(dto.getPriceChange());
        entity.setChangePercent(dto.getChangePercent());
        return entity;
    }



}
