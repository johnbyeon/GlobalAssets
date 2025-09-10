package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.CandleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandleRepository extends JpaRepository<CandleEntity, String> {
    List<CandleEntity> findTop200ByMarketAndFrameOrderByTDesc(String market, String frame);
    long countByMarketAndFrame(String market, String frame);
}
