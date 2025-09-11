package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "prices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PricesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;

    /** 단순히 asset_id 정수로 보관 (연관관계 안 잡음) */
    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @Column(nullable = false)
    private Timestamp timestamp;

    private Double open;
    private Double close;
    private Double high;
    private Double low;
}
