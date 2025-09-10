package com.fourMan.GlobalAssets.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TickerDto {
    private String market;
    private BigDecimal price;
    private Long ts;
    private String source;

    // 일일 요약용
    private BigDecimal prevClose;   // prev_closing_price
    private BigDecimal delta;      // signed_change_price
    private BigDecimal deltaRate;  // signed_change_rate (0.0123 => 1.23%)
}
