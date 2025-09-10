package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
@Builder
@Entity
@Getter @Setter
//@Table(
//        name = "daily_summary",
//        uniqueConstraints = @UniqueConstraint(
//                name = "uk_daily_summary_asset_date",
//                columnNames = {"asset_id", "summary_date"}
//        ),
//        indexes = {
//                @Index(name = "idx_daily_summary_asset_date", columnList = "asset_id, summary_date")
//        }
//)
public class DailySummaryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ 자산 FK: AssetsEntity로 통일
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private AssetsEntity asset;

    @Column(name = "summary_date", nullable = false)
    private LocalDate date;

    @Column(name = "close_price", precision = 18, scale = 6, nullable = false)
    private BigDecimal price;

    @Column(name = "prev_close", precision = 18, scale = 6)
    private BigDecimal prevClose;

    @Column(name = "delta", precision = 18, scale = 6)
    private BigDecimal delta;

    @Column(name = "delta_percent", precision = 9, scale = 4)
    private BigDecimal deltaPercent;
}
