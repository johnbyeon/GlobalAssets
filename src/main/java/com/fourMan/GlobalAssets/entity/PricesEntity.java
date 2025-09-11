package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder

public class PricesEntity {

    PricesEntity(){

    }

    public PricesEntity(Long priceId, Long assetId, Timestamp timestamp, BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low) {
        this.priceId = priceId;
        this.assetId = assetId;
        this.timestamp = timestamp;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

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

    public PricesEntity(Long priceId, Long assetId, Timestamp timestamp, BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low) {
        this.priceId = priceId;
        this.assetId = assetId;
        this.timestamp = timestamp;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    @Column(nullable = false)
    private BigDecimal low;
}
