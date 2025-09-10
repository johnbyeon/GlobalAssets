package com.fourMan.GlobalAssets.repository;

import com.fourMan.GlobalAssets.entity.AdvertisementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<AdvertisementEntity, Long> {
}