package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dao.PricesDao;
import com.fourMan.GlobalAssets.dto.PricesDto;
import com.fourMan.GlobalAssets.entity.PricesEntity;
import com.fourMan.GlobalAssets.repository.PricesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PricesService {
    private final PricesRepository pricesRepository;
    private final PricesDao pricesDao;

    public void insertPrices(PricesDto dto){
        pricesDao.insertPricesEntity(PricesDto.fromDto(dto));
    }
    public void deletePrices(Long priceId){
        pricesDao.deletePricesEntity(priceId);
    }

    public void updatePrices(PricesDto dto){
        pricesDao.insertPricesEntity(PricesDto.fromDto(dto));
    }
    public List<PricesDto> findAllByAssetId(Long assetId){
        List<PricesEntity> pricesEntities = pricesDao.findAllByAssetId(assetId);
        if(ObjectUtils.isEmpty(pricesEntities)){return null;}
        return pricesEntities.stream()
                .map(x->PricesDto.fromEntity(x))
                .toList();
    }
    public List<PricesDto> findTop20ByAssetIdOrderByTimestampDesc(Long assetId){
        List<PricesEntity> pricesEntities = pricesRepository.findTop20ByAssetIdOrderByTimestampDesc(assetId);
        if(ObjectUtils.isEmpty(pricesEntities))
        {
            return null;
        }
        return pricesEntities.stream()
                .map(x->PricesDto.fromEntity(x))
                .toList();
    }
}
