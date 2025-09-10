package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dto.CandleDto;
import com.fourMan.GlobalAssets.entity.PricesEntity;
import com.fourMan.GlobalAssets.repository.PriceRepository;
import com.fourMan.GlobalAssets.upbit.UpbitRest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceIngestService {

    private final UpbitRest upbitRest;
    private final AssetService assetService;
    private final PriceRepository priceRepo;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Transactional
    public int fetchAndStoreMinuteCandles(String upbitMarket, String frame, int count, String assetName) {
        Long assetId = assetService.getOrCreateAssetIdByUpbitMarket(upbitMarket, assetName);
        List<CandleDto> rows = upbitRest.candles(upbitMarket, frame, count);
        if (rows == null || rows.isEmpty()) return 0;

        int inserted = 0;
        for (CandleDto k : rows) {
            // Upbit ms(UTC) -> LocalDateTime(KST) 분 절삭
            LocalDateTime ts = Instant.ofEpochMilli(k.getT())
                    .atZone(KST).truncatedTo(ChronoUnit.MINUTES).toLocalDateTime();

            if (priceRepo.existsByAssetIdAndTimestamp(assetId, ts)) continue;

            priceRepo.save(PricesEntity.builder()
                    .assetId(assetId)
                    .timestamp(Timestamp.valueOf(ts))
                    .open(k.getO()).high(k.getH()).low(k.getL()).close(k.getC())
                    .build());
            inserted++;
        }
        return inserted;
    }
}
