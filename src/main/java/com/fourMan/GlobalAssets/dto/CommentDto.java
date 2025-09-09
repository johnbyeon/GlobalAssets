package com.fourMan.GlobalAssets.dto;

import com.fourMan.GlobalAssets.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long userId;
    private String commentBody;
    private Timestamp createTime;
    private Timestamp updateTime;

    public static CommentDto fromEntity(CommentEntity entity) {
        return new CommentDto(
                entity.getCommentId(),
                entity.getUserId(),
                entity.getCommentBody(),
                entity.getCreateTime(),
                entity.getUpdateTime());
    }

    public static CommentEntity fromDto(CommentDto dto) {
        CommentEntity comment = new CommentEntity();
        comment.setCommentId(dto.getCommentId());
        comment.setUserId(dto.getUserId());
        comment.setCommentBody(dto.getCommentBody());
        comment.setCreateTime(dto.getCreateTime());
        comment.setUpdateTime(dto.getUpdateTime());
        return comment;
    }
}
