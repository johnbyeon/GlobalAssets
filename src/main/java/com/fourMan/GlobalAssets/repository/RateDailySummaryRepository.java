package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RateDailySummaryRepository extends JpaRepository<DailySummaryEntity, Long> {

    @Query(value = """
            SELECT *
            FROM daily_summary
            WHERE asset_id = :assetId
            ORDER BY ds.summary_date DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<DailySummaryEntity> findRecentByAsset(@Param("assetId") Long assetId,
                                               @Param("limit") int limit);

    Optional<DailySummaryEntity> findByAssetIdAndDate(Long assetId, LocalDate date);

    // 이전(직전) 영업일 데이터 1건
    Optional<DailySummaryEntity> findTop1ByAssetIdAndDateLessThanOrderByDateDesc(Long assetId, LocalDate date);
}
