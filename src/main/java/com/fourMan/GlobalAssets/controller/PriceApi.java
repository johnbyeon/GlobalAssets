package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.entity.PricesEntity;
import com.fourMan.GlobalAssets.repository.AssetRepository;
import com.fourMan.GlobalAssets.repository.PriceRepository;
import com.fourMan.GlobalAssets.util.Symbols;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/db")
public class PriceApi {

    private final AssetRepository assetRepo;
    private final PriceRepository priceRepo;

    @GetMapping("/prices/latest")
    public List<Map<String, Object>> latest200(@RequestParam String market) {
        String internal = Symbols.toInternal(market); // KRW-BTC → BTCKRW
        AssetsEntity a = assetRepo.findBySymbol(internal).orElse(null);
        if (a == null) return List.of();

        List<PricesEntity> desc = priceRepo.findTop200ByAssetIdOrderByTimestampDesc(a.getId());
        Collections.reverse(desc);

        List<Map<String, Object>> out = new ArrayList<>();
        for (PricesEntity p : desc) {
            Map<String, Object> m = new HashMap<>();
            m.put("market", market);
            m.put("t", (p.getTimestamp().toLocalDateTime()).atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli());
            m.put("o", p.getOpen());
            m.put("h", p.getHigh());
            m.put("l", p.getLow());
            m.put("c", p.getClose());
            out.add(m);
        }
        return out;
    }
}
