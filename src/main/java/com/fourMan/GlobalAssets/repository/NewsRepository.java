package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
    List<NewsEntity> findTop20ByAssetIdOrderByTimestampDesc(Long assetId);
}
