package com.fourMan.GlobalAssets.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prices", uniqueConstraints = @UniqueConstraint(
        name = "uk_asset_day", columnNames = {"asset_id","timestamp"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RatePrice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "asset_id", nullable = false)
    private RateAsset asset;

    @Column(name = "`timestamp`", nullable = false)
    private LocalDate timestamp;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal open;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal close;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal high;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal low;
}
