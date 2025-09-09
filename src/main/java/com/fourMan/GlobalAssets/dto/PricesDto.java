package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.PricesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricesDto {

    private Long priceId;

    private Long assetId;

    private Timestamp timestamp;

    private Double open;

    private Double close;

    private Double high;

    private Double low;
    public static PricesDto fromEntity(PricesEntity entity) {
        return new PricesDto(
                entity.getPriceId(),
                entity.getAssetId(),
                entity.getTimestamp(),
                entity.getOpen(),
                entity.getClose(),
                entity.getHigh(),
                entity.getLow()
        );
    }

    // DTO -> Article
    public static PricesEntity fromDto(PricesDto dto) {
        PricesEntity entity = new PricesEntity();
        entity.setPriceId(dto.getPriceId());
        entity.setAssetId(dto.getAssetId());
        entity.setTimestamp(dto.getTimestamp());
        entity.setOpen(dto.getOpen());
        entity.setClose(dto.getClose());
        entity.setHigh(dto.getHigh());
        entity.setLow(dto.getLow());
        return entity;
    }
}

