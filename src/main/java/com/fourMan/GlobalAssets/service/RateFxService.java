// src/main/java/com/fourMan/GlobalAssets/service/RateFxService.java
package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.config.RateTwelveDataClient;
import com.fourMan.GlobalAssets.dto.DailySummaryDto;
import com.fourMan.GlobalAssets.model.RateAsset;
import com.fourMan.GlobalAssets.model.RateDailySummary;
import com.fourMan.GlobalAssets.model.RatePrice;
import com.fourMan.GlobalAssets.repository.RateAssetsRepository;
import com.fourMan.GlobalAssets.repository.RateDailySummaryRepository;
import com.fourMan.GlobalAssets.repository.RatePriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RateFxService {

    private final RateTwelveDataClient tdClient;
    private final RateAssetsRepository assetRepo;
    private final RatePriceRepository priceRepo;
    private final RateDailySummaryRepository summaryRepo;

    // 표시 배율 (원/엔 등 자리수 조정)
    private static final Map<String, Integer> DISPLAY_MULTIPLIER = Map.of(
            "USD/KRW", 1,
            "JPY/KRW", 1,
            "EUR/KRW", 1
    );

    /* ... (refreshAllFromApi 등 기존 메서드는 유지, 임포트만 수정) ... */

    @Transactional(readOnly = true)
    public List<DailySummaryDto> loadRowsFor(String symbol, int days) {
        RateAsset asset = assetRepo.findBySymbol(symbol).orElseThrow();
        List<RateDailySummary> list = summaryRepo
                .findTop15ByAssetIdOrderByTimestampDesc(asset.getAssetId());

        int mult = DISPLAY_MULTIPLIER.getOrDefault(symbol, 1);

        return list.stream()
                .limit(days)
                .map(s -> {
                    // price = prevClose + change
                    BigDecimal prevClose = Optional.ofNullable(s.getPrevClose()).orElse(BigDecimal.ZERO);
                    BigDecimal change     = Optional.ofNullable(s.getChange()).orElse(BigDecimal.ZERO);
                    BigDecimal price      = prevClose.add(change);

                    // 배율 반영 후 Double 변환
                    double scaledPrice  = price.multiply(BigDecimal.valueOf(mult)).doubleValue();
                    Double scaledChange = (s.getChange() == null) ? null
                            : s.getChange().multiply(BigDecimal.valueOf(mult)).doubleValue();
                    Double changePct    = (s.getChangePercent() == null) ? null : s.getChangePercent().doubleValue();

                    LocalDate d = s.getTimestamp(); // LocalDate
                    Timestamp ts = (d == null) ? null : Timestamp.valueOf(d.atStartOfDay());

                    return new DailySummaryDto(
                            asset.getAssetId(),
                            ts,
                            scaledPrice,
                            scaledChange,
                            changePct
                    );
                })
                .collect(toList());
    }
}
