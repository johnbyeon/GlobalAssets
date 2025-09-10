package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssetsDto {
    private Long id;
    private String code;
    private String symbol;
    private String name;
    private String category;

    // ★ 뉴스 묶음: 응답 시에만 채움 (DB/엔티티 비의존)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<RateNewsItem> news;

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
        AssetsEntity e = AssetsEntity.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .symbol(dto.getSymbol())
                .name(dto.getName())
                .category(dto.getCategory())
                .build();
        return e;
    }
}
