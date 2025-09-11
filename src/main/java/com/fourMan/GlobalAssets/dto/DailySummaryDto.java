package com.fourMan.GlobalAssets.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailySummaryDto {

    private Long id;
    private Long assetId;
    private LocalDate date;
    private BigDecimal price;
    private BigDecimal priceChange;
    private BigDecimal changePercent;


    private DailySummaryDto toDto(DailySummaryEntity e) {
        DailySummaryDto dto = new DailySummaryDto();
        dto.setId(e.getId());
        dto.setAssetId(e.getAssetId());
        dto.setDate(e.getDate());
        dto.setPrice(e.getPrice());
        dto.setPriceChange(e.getPriceChange());
        dto.setChangePercent(e.getChangePercent());
        return dto;

    }

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public LocalDate getDate() { return date; }            // ← 서비스에서 메서드 참조 사용
    public void setDate(LocalDate date) { this.date = date; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getPriceChange() { return priceChange; }
    public void setPriceChange(BigDecimal priceChange) { this.priceChange = priceChange; }
    public BigDecimal getChangePercent() { return changePercent; }
    public void setChangePercent(BigDecimal changePercent) { this.changePercent = changePercent; }

    // 호환용
    public LocalDate getSummaryDate() { return date; }
    public Timestamp getTimestamp() { return (date == null) ? null : Timestamp.valueOf(date.atStartOfDay()); }
    public Double getPriceAsDouble() { return price == null ? null : price.doubleValue(); }
    public Double getPriceChangeAsDouble() { return priceChange == null ? null : priceChange.doubleValue(); }
    public Double getChangePercentAsDouble() { return changePercent == null ? null : changePercent.doubleValue(); }
}
