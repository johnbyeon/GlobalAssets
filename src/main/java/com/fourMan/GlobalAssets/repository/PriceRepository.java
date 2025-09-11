package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.PricesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<PricesEntity, Long> {
    List<PricesEntity> findTop300ByAssetIdOrderByTimestampDesc(Long assetId);
}
