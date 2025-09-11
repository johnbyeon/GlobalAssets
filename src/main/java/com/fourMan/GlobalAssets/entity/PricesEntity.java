package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long priceId;

    /** 단순히 asset_id 정수로 보관 (연관관계 안 잡음) */
    @Column( nullable = false)
    private Long assetId;

    @Column(nullable = false)
    private Timestamp timestamp;

    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
}
