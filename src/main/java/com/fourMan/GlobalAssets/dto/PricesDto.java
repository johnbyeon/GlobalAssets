package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.Prices;
import com.fourMan.GlobalAssets.entity.Viewers;
import jakarta.persistence.Column;
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
    public static PricesDto fromEntity(Prices entity) {
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
    public static Prices fromDto(PricesDto dto) {
        Prices entity = new Prices();
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

