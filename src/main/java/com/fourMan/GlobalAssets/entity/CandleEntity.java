package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table( name = "candles",
        indexes = {
                @Index
                (name = "idx_candle_mkt_frame_slot",
                 columnList = "market, time_frame, slot_time_ms")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandleEntity {

    @Id
    @Column(name = "candle_id", length = 64) // ex) KRW-BTC|1m|1725436200000
    private String id;

    @Column(name = "market", nullable = false, length = 20) // ex) KRW-BTC
    private String market;

    @Column(name = "time_frame", nullable = false, length = 10) // ex) 1m,5m,15m,1h,4h,1d
    private String frame;

    @Column(name = "slot_time_ms", nullable = false) // 슬롯 시작 epoch ms
    private long t;

    @Column(name = "open_price", nullable = false)
    private double o;

    @Column(name = "high_price", nullable = false)
    private double h;

    @Column(name = "low_price", nullable = false)
    private double l;

    @Column(name = "close_price", nullable = false)
    private double c;

    @Column(name = "volume", nullable = false) // 체결량
    private double v;
}
