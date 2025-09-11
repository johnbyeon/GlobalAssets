package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.PricesEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface PricesRepository extends JpaRepository<PricesEntity, Long> {

    boolean existsByAssetIdAndTimestamp(Long assetId, LocalDateTime ts);

    List<PricesEntity> findTop200ByAssetIdOrderByTimestampDesc(Long assetId);

    List<PricesEntity> findTop20ByAssetIdOrderByTimestampDesc(Long assetId);

    @Query("select p from PricesEntity p where p.assetId=:assetId and p.timestamp between :from and :to order by p.timestamp asc")
    List<PricesEntity> findRangeAsc(Long assetId, LocalDateTime from, LocalDateTime to);


}
