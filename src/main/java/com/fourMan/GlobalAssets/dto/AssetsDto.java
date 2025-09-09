package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetsDto {

    private Long assetId;
    //종목코드


    private String symbol;
    //종목이름

    private String name;

    public static AssetsDto fromEntity(AssetsEntity entity) {
        return new AssetsDto(
                entity.getAssetId(),
                entity.getSymbol(),
                entity.getName()
        );
    }

    // DTO -> Article
    public static AssetsEntity fromDto(AssetsDto dto) {
        AssetsEntity entity = new AssetsEntity();
        entity.setAssetId(dto.getAssetId());
        entity.setSymbol(dto.getSymbol());
        entity.setName(dto.getName());
        return entity;
    }
}
