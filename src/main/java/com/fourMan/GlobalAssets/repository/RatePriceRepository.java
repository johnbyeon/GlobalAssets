package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.model.RateAsset;
import com.fourMan.GlobalAssets.model.RatePrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RatePriceRepository extends JpaRepository<RatePrice, Long> {
    Optional<RatePrice> findByAssetAndTimestamp(RateAsset asset, LocalDate timestamp);
    List<RatePrice> findTop10ByAssetOrderByTimestampDesc(RateAsset asset);
}
