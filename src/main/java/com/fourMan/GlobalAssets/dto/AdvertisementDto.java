package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.AdvertisementEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDto {

    private Long adventId;
    //광고 순번
    private Long sortNum;
    //광고 이미지 주소
    private String imagePath;

    public static AdvertisementDto fromComment(AdvertisementEntity entity) {
        return new AdvertisementDto(
                entity.getAdventId(),
                entity.getSortNum(),
                entity.getImagePath()
        );
    }

    public static AdvertisementEntity fromDto(AdvertisementDto dto) {
        AdvertisementEntity entity = new AdvertisementEntity();
        entity.setAdventId(dto.getAdventId());
        entity.setSortNum(dto.getSortNum());
        entity.setImagePath(dto.getImagePath());
        return entity;
    }
}
