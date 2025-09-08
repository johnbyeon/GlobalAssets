package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.News;
import com.fourMan.GlobalAssets.entity.Viewers;
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

    public static NewsDto fromEntity(News entity) {
        return new NewsDto(
                entity.getNewsId(),
                entity.getAssetId(),
                entity.getTimestamp(),
                entity.getTitle(),
                entity.getUrlLink()
        );
    }

    // DTO -> Article
    public static News fromDto(NewsDto dto) {
        News entity = new News();
        entity.setNewsId(dto.getNewsId());
        entity.setAssetId(dto.getAssetId());
        entity.setTimestamp(dto.getTimestamp());
        entity.setTitle(dto.getTitle());
        entity.setUrlLink(dto.getUrlLink());
        return entity;
    }
}
