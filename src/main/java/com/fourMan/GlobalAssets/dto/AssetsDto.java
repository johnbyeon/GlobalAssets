package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.AssetsEntity;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AssetsDto {
    private Long id;
    private String code;
    private String name;
    private String symbol;
    private String category;

    public static AssetsDto fromEntity(AssetsEntity e) {
        return AssetsDto.builder()
                .id(e.getId())
                .code(e.getCode())
                .name(e.getName())
                .symbol(e.getSymbol())
                .category(e.getCategory())
                .build();
    }

    public static AssetsEntity toEntity(AssetsDto dto) {
        return AssetsEntity.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .symbol(dto.getSymbol())
                .category(dto.getCategory())
                .build();
    }
}
