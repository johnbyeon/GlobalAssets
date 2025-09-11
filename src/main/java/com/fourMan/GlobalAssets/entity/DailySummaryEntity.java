package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // JPA용 기본 생성자
@AllArgsConstructor
@Builder                                             // ← 이거 추가!
@Entity
@Table(name = "daily_summary")
public class DailySummaryEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @Column(name = "summary_date", nullable = false) // DB가 summary_date면 매핑
    private LocalDate date;

    @Column(name = "prev_close")
    private BigDecimal prevClose;

    @Column(name = "close_price", nullable = false)  // DB가 close_price면 매핑
    private BigDecimal price;

    @Column(name = "`change`", nullable = false)     // 예약어면 백틱으로
    private BigDecimal priceChange;

    @Column(name = "change_percent", nullable = false)
    private BigDecimal changePercent;
}
