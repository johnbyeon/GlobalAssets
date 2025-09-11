package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.PricesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricesDto {

    private Long priceId;

    private Long assetId;

    private Timestamp timestamp;

    private BigDecimal open;

    private BigDecimal close;

    private BigDecimal high;

    private BigDecimal low;
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
        PricesEntity entity = PricesEntity.builder()
                .priceId(dto.getPriceId())
                .assetId(dto.getAssetId())
                .timestamp(dto.getTimestamp())
                .open(dto.getOpen())
                .close(dto.getClose())
                .high(dto.getHigh())
                .low(dto.getLow())
                .build();

        return entity;
    }
}

