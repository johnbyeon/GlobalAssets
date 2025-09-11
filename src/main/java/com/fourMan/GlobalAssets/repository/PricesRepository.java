package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.PricesEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface PricesRepository extends JpaRepository<PricesEntity, Long> {

    // 엔티티 필드명: timestamp (java.sql.Timestamp)
    boolean existsByAssetIdAndTimestamp(Long assetId, Timestamp ts);

    List<PricesEntity> findTop200ByAssetIdOrderByTimestampDesc(Long assetId);

    List<PricesEntity> findTop20ByAssetIdOrderByTimestampDesc(Long assetId);

    @Query("select p from PricesEntity p where p.assetId = :assetId and p.timestamp between :from and :to order by p.timestamp asc")
    List<PricesEntity> findRangeAsc(Long assetId, Timestamp from, Timestamp to);


}
