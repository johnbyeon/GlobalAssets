package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    //종목번호
    private Long assetId;
    //유저 id 숫자
    private Long userId;
    //공개여부 운영자가 정함
    private Boolean articleStatus;

    @Column(length = 30)
    private String title;

    @Column(length = 5000)
    private String body;
    //생성시간
    private Timestamp createTime;
    //수정시간
    private Timestamp updateTime;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "articleEntity",
            cascade = {CascadeType.PERSIST,
                    CascadeType.REMOVE})
    List<CommentEntity> commentEntities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "articleEntity",
            cascade = {CascadeType.PERSIST,
                    CascadeType.REMOVE})
    List<ViewersEntity> viewers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "articleEntity",
            cascade = {CascadeType.PERSIST,
                    CascadeType.REMOVE})
    List<LikesEntity> LikesEntity = new ArrayList<>();
}
