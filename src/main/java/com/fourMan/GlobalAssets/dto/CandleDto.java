package com.fourMan.GlobalAssets.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CandleDto {
    private String market; // KRW-BTC
    private String frame;  // 1m/5m/15m/1h/4h/1d
    private Long t;        // epoch millis (UTC)
    private Double o, h, l, c, v; // open/high/low/close/volume
}
