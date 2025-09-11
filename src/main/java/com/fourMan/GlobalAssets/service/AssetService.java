package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.dao.AssetsDao;
import com.fourMan.GlobalAssets.dto.AssetsDto;
import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fourMan.GlobalAssets.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetsDao assetsDao;
    private final AssetRepository assetRepository;
    public AssetsDto findOneAssets(Long id){
        AssetsEntity entity = assetsDao.getOneAssetsEntity(id);
        if(!ObjectUtils.isEmpty(entity)) {return AssetsDto.fromEntity(entity);}
        return null;
    }
    public AssetsDto findOneSymbol(String symbol){
        Optional<AssetsEntity> opt  = assetRepository.findBySymbol(symbol);
        if (opt.isPresent()) {
            AssetsEntity asset = opt.get();
            return AssetsDto.fromEntity(asset);// 안전하게 사용 가능
        }
        return null;
    }
    public List<AssetsDto> findAllByCategory(String category){
        List<AssetsEntity> assetsEntityList  = assetsDao.findAllByCategory(category);
        if (!ObjectUtils.isEmpty(assetsEntityList)) {
            return assetsEntityList.stream()
                    .map(x->AssetsDto.fromEntity(x))
                    .toList();// 안전하게 사용 가능
        }
        return Collections.emptyList();
    }
    public List<AssetsDto> findAllByCode(String code){
        List<AssetsEntity> assetsEntityList  = assetsDao.findAllByCode(code);
        if (!ObjectUtils.isEmpty(assetsEntityList)) {
            return assetsEntityList.stream()
                    .map(x->AssetsDto.fromEntity(x))
                    .toList();// 안전하게 사용 가능
        }
        return Collections.emptyList();
    }

    public void insertAssets(AssetsDto dto){
        assetsDao.insertAssetsEntity(AssetsDto.fromDto(dto));
    }
}
