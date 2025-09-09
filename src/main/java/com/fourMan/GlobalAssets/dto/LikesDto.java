package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.LikesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikesDto {
    private Long userId;

    public static LikesDto fromEntity(LikesEntity entity) {
        return new LikesDto(
                entity.getUserId()
                );
    }

    // DTO -> Article
    public static LikesEntity fromDto(LikesDto dto) {
        LikesEntity entity = new LikesEntity();
        entity.setUserId(dto.getUserId());
        return entity;
    }
}
