package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private Long articleId;
    //종목번호
    private Long assetId;
    //유저 id 숫자
    private Long userId;
    //공개여부 운영자가 정함
    private Boolean articleStatus;

    private String title;

    private String body;
    //생성시간
    private Timestamp createTime;
    //수정시간
    private Timestamp updateTime;

    List<CommentDto> comments = new ArrayList<>();

    List<ViewersDto> viewers = new ArrayList<>();

    List<LikesDto> Likes = new ArrayList<>();

    // 엔티티 -> DTO
    public static ArticleDto fromEntity(Article entity) {
        return new ArticleDto(
                entity.getArticleId(),
                entity.getAssetId(),
                entity.getUserId(),
                entity.getArticleStatus(),
                entity.getTitle(),
                entity.getBody(),
                entity.getCreateTime(),
                entity.getUpdateTime(),
                entity.getComments()
                .stream()
                .map(x -> CommentDto.fromEntity(x))
                .toList(),
                entity.getViewers()
                        .stream()
                        .map(x->ViewersDto.fromEntity(x))
                        .toList(),
                entity.getLikes()
                        .stream()
                        .map(x->LikesDto.fromEntity(x))
                        .toList()
        );
    }

    // DTO -> Article
    public static Article fromDto(ArticleDto dto) {
        Article entity = new Article();
        entity.setArticleId(dto.getArticleId());
        entity.setAssetId(dto.getAssetId());
        entity.setUserId(dto.getUserId());
        entity.setArticleStatus(dto.getArticleStatus());
        entity.setTitle(dto.getTitle());
        entity.setBody(dto.getBody());
        entity.setCreateTime(dto.getCreateTime());
        entity.setUpdateTime(dto.getUpdateTime());
        return entity;
    }
}
