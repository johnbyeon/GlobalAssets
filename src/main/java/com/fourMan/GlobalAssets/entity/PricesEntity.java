package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
public class PricesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priceId;
    //종목 번호
    @Column(nullable = false)
    private Long assetId;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    private BigDecimal open;

    @Column(nullable = false)
    private BigDecimal close;

    @Column(nullable = false)
    private BigDecimal high;

    @Column(nullable = false)
    private BigDecimal low;
}
