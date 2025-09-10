package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepo;

    @Transactional
    public Long getOrCreateAssetIdByUpbitMarket(String upbitMarket, String defaultName) {
        String internal = Symbols.toInternal(upbitMarket); // KRW-BTC -> BTCKRW
        return assetRepo.findBySymbol(internal)
                .map(AssetsEntity::getId)
                .orElseGet(() -> assetRepo.save(
                        AssetsEntity.builder()
                                .symbol(internal)
                                .name(defaultName)
                                .build()
                ).getId());
    }
}
