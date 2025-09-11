package com.fourMan.GlobalAssets.scheduler;


import com.fourMan.GlobalAssets.config.ADMIN;
import com.fourMan.GlobalAssets.dto.AssetsDto;
import com.fourMan.GlobalAssets.dto.PricesDto;
import com.fourMan.GlobalAssets.dto.UpbitMinuteCandleDto;
import com.fourMan.GlobalAssets.service.AssetService;
import com.fourMan.GlobalAssets.service.PricesService;
import com.fourMan.GlobalAssets.service.UpbitCandleClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.invoke.SwitchPoint;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpbitCandleScheduler {
    private final UpbitCandleClientService candleClient;
    private final AssetService assetService;
    private final PricesService pricesService;
    public static Map<String, Long> cryptoAssetsId = new HashMap<>();

    // 1분마다 실행 (60,000ms)
    @Scheduled(fixedRate = 10000)
    public void fetchOneMinuteCandle() {
            if(ObjectUtils.isEmpty(cryptoAssetsId)){
                return;
            }

        try {
            for (int i = 0; i < ADMIN.INIT_CRYPTO.CODES.length; i++) {
                UpbitMinuteCandleDto[] candles = candleClient.fetchOneMinuteAgo(ADMIN.INIT_CRYPTO.CODES[i], 1);
                if (candles != null && candles.length > 0) {
                    UpbitMinuteCandleDto candle = candles[0];
                    log.info("📊 Upbit Candle - Market: {}, Time: {}, highPrice: {}, lowPrice: {}, openingPrice: {}, tradePrice: {}",
                            candle.market(), candle.candleDateTimeKst(), candle.highPrice(), candle.lowPrice(), candle.openingPrice(), candle.tradePrice());
                    Long assetid = cryptoAssetsId.get(ADMIN.INIT_CRYPTO.CODES[i]);
                    if (!ObjectUtils.isEmpty(assetid)) {
                        PricesDto dto = new PricesDto();
                        dto.setAssetId(assetid);
                        dto.setHigh(candle.highPrice());
                        dto.setLow(candle.lowPrice());
                        dto.setOpen(candle.openingPrice());
                        dto.setClose(candle.tradePrice());
                        LocalDateTime ldt = LocalDateTime.parse(candle.candleDateTimeKst(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        log.info("localdatetime값:{} ",ldt);
                        dto.setTimestamp(Timestamp.valueOf(ldt));
                        log.info("dto : {}", dto);
                        if (ObjectUtils.isEmpty(pricesService.findAllByAssetIdAndTimestamp(dto.getAssetId(),dto.getTimestamp()))) {
                            pricesService.insertPrices(dto);
                        }
                        else {
                            log.warn("⚠ 코인데이터 중복으로 데이터 만들지 않음");
                        }
                    }

                } else {
                    log.warn("⚠ No candle data returned");
                }
            }

        } catch (Exception e) {
            log.error("❌ Failed to fetch candle", e);
        }
    }
}
