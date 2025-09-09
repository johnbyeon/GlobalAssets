package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AssetsDto {
    private Long id;
    private String code;
    private String symbol;
    private String name;
    private String category;

    public static AssetsDto fromEntity(AssetsEntity entity) {
        if (entity == null) return null;
        AssetsDto dto = new AssetsDto();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setSymbol(entity.getSymbol());
        dto.setName(entity.getName());
        dto.setCategory(entity.getCategory());
        return dto;
    }

    public static AssetsEntity fromDto(AssetsDto dto) {
        if (dto == null) return null;
        AssetsEntity e = new AssetsEntity();
        e.setId(dto.getId());
        e.setCode(dto.getCode());
        e.setSymbol(dto.getSymbol());
        e.setName(dto.getName());
        e.setCategory(dto.getCategory());
        return e;
    }
}
