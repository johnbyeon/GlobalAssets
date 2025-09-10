package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.TickerDto;
import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import com.fourMan.GlobalAssets.repository.DailySummaryRepository;
import com.fourMan.GlobalAssets.upbit.UpbitRest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service @RequiredArgsConstructor
public class DailySummaryService {

    private final UpbitRest upbitRest;
    private final AssetService assetService;
    private final DailySummaryRepository dailyRepo;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Transactional
    public void refreshDaily(String upbitMarket, String assetName) {
        Long assetId = assetService.getOrCreateAssetIdByUpbitMarket(upbitMarket, assetName);
        TickerDto t = upbitRest.tickers(java.util.List.of(upbitMarket)).stream().findFirst().orElse(null);
        if (t == null) return;

        BigDecimal prevClose     = t.getPrevClose() != null ? t.getPrevClose() : BigDecimal.valueOf(0.0);
        BigDecimal delta        = t.getDelta() != null ? t.getDelta() : BigDecimal.valueOf(0.0);
        BigDecimal deltaPercent = t.getDeltaRate() != null ? t.getDeltaRate().multiply(BigDecimal.valueOf(100.0)) : BigDecimal.valueOf(0.0);

        LocalDate date = Instant.ofEpochMilli(t.getTs()).atZone(KST).toLocalDate();

        dailyRepo.save(DailySummaryEntity.builder()
                .id(assetId)
                .date(date)
                .prevClose(prevClose)
                .delta(delta)
                .deltaPercent(deltaPercent)
                .build());
    }
}
