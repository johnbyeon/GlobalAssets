package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.Viewers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewersDto {
    private Long userId;

    public static ViewersDto fromEntity(Viewers entity) {
        return new ViewersDto(
                entity.getUserId()
        );
    }

    // DTO -> Article
    public static Viewers fromDto(ViewersDto dto) {
        Viewers entity = new Viewers();
        entity.setUserId(dto.getUserId());
        return entity;
    }
}
