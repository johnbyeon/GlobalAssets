package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_summary",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_daily_summary_asset_date", columnNames = {"asset_id", "date"})
        },
        indexes = {
                @Index(name = "idx_daily_summary_asset_date", columnList = "asset_id,date")
        })
public class DailySummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "prev_close")
    private BigDecimal prevClose;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "`change`")
    private BigDecimal priceChange;

    @Column(name = "change_percent")
    private BigDecimal changePercent;

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public BigDecimal getPrevClose() { return prevClose; }
    public void setPrevClose(BigDecimal prevClose) { this.prevClose = prevClose; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getPriceChange() { return priceChange; }
    public void setPriceChange(BigDecimal priceChange) { this.priceChange = priceChange; }
    public BigDecimal getChangePercent() { return changePercent; }
    public void setChangePercent(BigDecimal changePercent) { this.changePercent = changePercent; }

}
