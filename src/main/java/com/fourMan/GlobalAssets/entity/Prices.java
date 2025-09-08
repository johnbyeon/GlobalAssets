package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Prices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priceId;
    //종목 번호
    @Column(nullable = false)
    private Long assetId;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    private Double open;

    @Column(nullable = false)
    private Double close;

    @Column(nullable = false)
    private Double high;

    @Column(nullable = false)
    private Double low;
}
