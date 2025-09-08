package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.Likes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikesDto {
    private Long userId;

    public static LikesDto fromEntity(Likes entity) {
        return new LikesDto(
                entity.getUserId()
                );
    }

    // DTO -> Article
    public static Likes fromDto(LikesDto dto) {
        Likes entity = new Likes();
        entity.setUserId(dto.getUserId());
        return entity;
    }
}
