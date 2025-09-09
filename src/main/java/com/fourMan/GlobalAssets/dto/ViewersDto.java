package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.ViewersEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewersDto {
    private Long userId;

    public static ViewersDto fromEntity(ViewersEntity entity) {
        return new ViewersDto(
                entity.getUserId()
        );
    }

    // DTO -> Article
    public static ViewersEntity fromDto(ViewersDto dto) {
        ViewersEntity entity = new ViewersEntity();
        entity.setUserId(dto.getUserId());
        return entity;
    }
}
