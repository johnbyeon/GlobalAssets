package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Comment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long commentId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "article_id")
        private Article article;

        //유저 숫자 아이디
        private Long userId;
        //댓글본문
        private String commentBody;
        //댓글생성일
        private Timestamp createTime;
        //댓글 수정일
        private Timestamp updateTime;
}
