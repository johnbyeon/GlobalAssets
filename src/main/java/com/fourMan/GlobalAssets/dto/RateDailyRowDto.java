
package com.fourMan.GlobalAssets.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RateDailyRowDto {
    private LocalDate date;
    private String pairName;
    private BigDecimal price;
    private BigDecimal change;
    private BigDecimal changePercent;
    public boolean isUp() { return change != null && change.signum() > 0; }
    public boolean isDown() { return change != null && change.signum() < 0; }
}
