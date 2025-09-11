package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.config.RateTwelveClient;
import com.fourMan.GlobalAssets.dto.DailySummaryDto;
import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import com.fourMan.GlobalAssets.repository.RateDailySummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateFxService {

    private final RateTwelveClient twelve;
    private final RateDailySummaryRepository dailyRepo;

    // 자산ID 매핑 (미설정 시 기본값 사용)
    @Value("${app.fx.asset.usd:1}") private Long usdAssetId;
    @Value("${app.fx.asset.jpy:2}") private Long jpyAssetId;
    @Value("${app.fx.asset.eur:3}") private Long eurAssetId;
    @Value("${app.fx.asset.cny:4}") private Long cnyAssetId;
    @Value("${app.fx.asset.gbp:5}") private Long gbpAssetId;

    // 심볼 매핑 (미설정 시 기본값 사용)
    @Value("${app.fx.symbol.usd:USD/KRW}") private String usdSymbol;
    @Value("${app.fx.symbol.jpy:JPY/KRW}") private String jpySymbol;
    @Value("${app.fx.symbol.eur:EUR/KRW}") private String eurSymbol;
    @Value("${app.fx.symbol.cny:CNY/KRW}") private String cnySymbol;
    @Value("${app.fx.symbol.gbp:GBP/KRW}") private String gbpSymbol;

    /* ========================= 수집/업서트 ========================= */

    @Transactional
    public void ingest(String symbol, int days) {
        Long assetId = resolveAssetId(symbol);
        ingest(assetId, symbol, days);
    }

    @Transactional
    public void refreshAll(int days) {
        ingest(usdAssetId, usdSymbol, days);
        ingest(jpyAssetId, jpySymbol, days);
        ingest(cnyAssetId, cnySymbol, days);
        ingest(eurAssetId, eurSymbol, days);
        ingest(gbpAssetId, gbpSymbol, days);
    }

    @Transactional
    public void ingest(Long assetId, String symbol, int days) {
        List<RateTwelveClient.Bar> bars = twelve.fetchDailySeries(symbol, days);
        if (bars.isEmpty()) {
            log.warn("[FX] No data from TwelveData: {}", symbol);
            return;
        }
        BigDecimal prevClose = null;
        for (RateTwelveClient.Bar b : bars) {
            LocalDate date  = b.date();
            BigDecimal close = b.close();

            BigDecimal chg = null;
            BigDecimal pct = null;
            if (prevClose != null && prevClose.compareTo(BigDecimal.ZERO) != 0) {
                chg = close.subtract(prevClose);
                pct = chg.multiply(BigDecimal.valueOf(100))
                        .divide(prevClose, 4, RoundingMode.HALF_UP);
            }
            upsertDaily(assetId, date, prevClose, close, chg, pct);
            prevClose = close;
        }
        log.info("[FX] Upsert done: {} ({} rows)", symbol, bars.size());
    }

    private void upsertDaily(Long assetId, LocalDate date,
                             BigDecimal prevClose, BigDecimal price,
                             BigDecimal change, BigDecimal changePct) {
        dailyRepo.findByAssetIdAndDate(assetId, date).ifPresentOrElse(ds -> {
            if (prevClose != null) ds.setPrevClose(prevClose);
            if (price != null) ds.setPrice(price);
            if (change != null) ds.setPriceChange(change);
            if (changePct != null) ds.setChangePercent(changePct);
            dailyRepo.save(ds);
        }, () -> {
            DailySummaryEntity ds = new DailySummaryEntity();
            ds.setAssetId(assetId);
            ds.setDate(date);
            ds.setPrevClose(prevClose);
            ds.setPrice(price != null ? price : BigDecimal.ZERO);
            ds.setPriceChange(change != null ? change : BigDecimal.ZERO);
            ds.setChangePercent(changePct != null ? changePct : BigDecimal.ZERO);
            dailyRepo.save(ds);
        });
    }

    /* ========================= 조회/DTO ========================= */

    @Transactional(readOnly = true)
    public List<DailySummaryDto> recentOne(String symbol, int limit) {
        Long assetId = resolveAssetId(symbol);
        List<DailySummaryEntity> rows = dailyRepo.findByAssetIdOrderByDateDesc(assetId);
        if (limit > 0 && rows.size() > limit) rows = rows.subList(0, limit);
        return rows.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Map<String, List<DailySummaryDto>> recentMany(String symbolsCsv, int limit) {
        Map<String, List<DailySummaryDto>> out = new LinkedHashMap<>();
        for (String raw : symbolsCsv.split(",")) {
            String sym = raw.trim();
            if (sym.isEmpty()) continue;
            out.put(sym, recentOne(sym, limit));
        }
        return out;
    }

    private DailySummaryDto toDto(DailySummaryEntity e) {
        DailySummaryDto dto = new DailySummaryDto();
        dto.setDate(e.getDate());
        dto.setPrice(e.getPrice());
        dto.setPriceChange(e.getPriceChange());
        dto.setChangePercent(e.getChangePercent());
        return dto;
    }

    private Long resolveAssetId(String symbol) {
        if (symbol.equalsIgnoreCase(usdSymbol)) return usdAssetId;
        if (symbol.equalsIgnoreCase(jpySymbol)) return jpyAssetId;
        if (symbol.equalsIgnoreCase(cnySymbol)) return cnyAssetId;
        if (symbol.equalsIgnoreCase(eurSymbol)) return eurAssetId;
        if (symbol.equalsIgnoreCase(gbpSymbol)) return gbpAssetId;

        String s = symbol.replace(" ", "");
        if (s.equalsIgnoreCase("USD/KRW")) return usdAssetId;
        if (s.equalsIgnoreCase("JPY/KRW")) return jpyAssetId;
        if (s.equalsIgnoreCase("CNY/KRW")) return cnyAssetId;
        if (s.equalsIgnoreCase("EUR/KRW")) return eurAssetId;
        if (s.equalsIgnoreCase("GBP/KRW")) return gbpAssetId;

        throw new IllegalArgumentException("Unknown symbol: " + symbol);
    }
}
