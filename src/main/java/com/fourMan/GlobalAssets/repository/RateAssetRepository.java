package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RateAssetRepository extends JpaRepository<AssetsEntity, Long> {
    Optional<AssetsEntity> findBySymbol(String symbol);
    Optional<AssetsEntity> findByCode(String code);
}
