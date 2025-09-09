package com.fourMan.GlobalAssets.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_summary")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@IdClass(RateDailySummary.PK.class)
public class RateDailySummary {
    @Id @Column(name = "asset_id")
    private Long assetId;

    @Id
    @Column(name = "`timestamp`")
    private LocalDate timestamp;

    @Column(name = "prev_close", nullable = false, precision = 18, scale = 6)
    private BigDecimal prevClose;

    @Column(name = "`change`", nullable = false, precision = 18, scale = 6)
    private BigDecimal change;

    @Column(name = "change_percent", nullable = false, precision = 9, scale = 4)
    private BigDecimal changePercent;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class PK implements Serializable {
        private Long assetId;
        private LocalDate timestamp;
    }
}
