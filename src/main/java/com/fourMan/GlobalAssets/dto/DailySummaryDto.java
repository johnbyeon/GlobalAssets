package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DailySummaryDto {

    private Long id;
    private Long assetId;
    private LocalDate date;           // summaryDate와 동의
    private BigDecimal price;
    private BigDecimal priceChange;
    private BigDecimal changePercent;

    public static DailySummaryDto fromEntity(DailySummaryEntity e) {
        return DailySummaryDto.builder()
                .id(e.getId())
                .assetId(e.getAssetId())
                .date(e.getDate())
                .price(e.getPrice())
                .priceChange(e.getPriceChange())
                .changePercent(e.getChangePercent())
                .build();
    }

    public static DailySummaryEntity toEntity(DailySummaryDto d, AssetsEntity asset) {
        return DailySummaryEntity.builder()
                .id(d.getId())
                .assetId(asset.getId())
                .date(d.getDate())
                .price(d.getPrice())
                .priceChange(d.getPriceChange())
                .changePercent(d.getChangePercent())
                .build();
    }

    /** === 호환용 접근자들 (이전에 사용하던 이름들) === */
    public LocalDate getSummaryDate() { return date; }
    public Timestamp getTimestamp() { return (date == null) ? null : Timestamp.valueOf(date.atStartOfDay()); }
    public Double getPriceAsDouble() { return price == null ? null : price.doubleValue(); }
    public Double getPriceChangeAsDouble() { return priceChange == null ? null : priceChange.doubleValue(); }
    public Double getChangePercentAsDouble() { return changePercent == null ? null : changePercent.doubleValue(); }
}
