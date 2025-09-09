package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter @Setter
@Table(
        name = "asset",
        indexes = {
                @Index(name = "idx_asset_code", columnList = "code"),
                @Index(name = "idx_asset_symbol", columnList = "symbol")
        },
        uniqueConstraints = @UniqueConstraint(name = "uk_asset_code", columnNames = "code")
)
public class AssetsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // asset_id (뉴스/요약 FK로 사용)

    @Column(length = 60, nullable = false)
    private String code;        // 005930, 000660, 373220, BTC/KRW, NVDA...

    @Column(length = 60)
    private String symbol;      // AAPL, NVDA, BTC/KRW 등(없으면 null)

    @Column(length = 120, nullable = false)
    private String name;        // 삼성전자, SK하이닉스, 비트코인...

    @Column(length = 30)
    private String category;    // STOCK / CRYPTO / FX 등(선택)
}
