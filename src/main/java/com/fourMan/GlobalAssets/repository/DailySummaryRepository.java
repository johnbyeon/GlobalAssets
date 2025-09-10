package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.DailySummaryEntity;
import com.fourMan.GlobalAssets.entity.DailySummaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailySummaryRepository extends JpaRepository<DailySummaryEntity, DailySummaryKey> {}
