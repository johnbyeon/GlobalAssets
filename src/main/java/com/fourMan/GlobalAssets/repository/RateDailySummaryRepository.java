package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RateDailySummaryRepository extends JpaRepository<DailySummaryEntity, Long> {

    // 최신 N개 (date desc). limit는 컨트롤러/서비스에서 subList로 잘라써도 됨.
    List<DailySummaryEntity> findByAssetIdOrderByDateDesc(Long assetId);

    // 페이징 필요할 때
    Page<DailySummaryEntity> findByAssetIdOrderByDateDesc(Long assetId, Pageable pageable);

    // 업서트용
    Optional<DailySummaryEntity> findByAssetIdAndDate(Long assetId, LocalDate date);

    Optional<DailySummaryEntity> findTop1ByAssetIdOrderByDateDesc(Long assetId);

    Optional<DailySummaryEntity> findTop1ByAssetIdAndDateLessThanOrderByDateDesc(Long assetId, LocalDate date);
}
