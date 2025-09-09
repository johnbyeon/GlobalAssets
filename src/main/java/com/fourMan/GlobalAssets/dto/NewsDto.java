package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.NewsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    private Long newsId;
    //종목번호 어떤종목의 뉴스인가??
    private Long assetId;
    //뉴스날짜
    private Timestamp timestamp;

    private String title;

    private String urlLink;

    public static NewsDto fromEntity(NewsEntity entity) {
        return new NewsDto(
                entity.getNewsId(),
                entity.getAssetId(),
                entity.getTimestamp(),
                entity.getTitle(),
                entity.getUrlLink()
        );
    }

    // DTO -> Article
    public static NewsEntity fromDto(NewsDto dto) {
        NewsEntity entity = new NewsEntity();
        entity.setNewsId(dto.getNewsId());
        entity.setAssetId(dto.getAssetId());
        entity.setTimestamp(dto.getTimestamp());
        entity.setTitle(dto.getTitle());
        entity.setUrlLink(dto.getUrlLink());
        return entity;
    }
}
