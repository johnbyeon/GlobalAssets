package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.w3c.dom.stylesheets.LinkStyle;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Article {

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
            mappedBy = "article",
            cascade = {CascadeType.PERSIST,
                    CascadeType.REMOVE})
    List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "article",
            cascade = {CascadeType.PERSIST,
                    CascadeType.REMOVE})
    List<Viewers> viewers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "article",
            cascade = {CascadeType.PERSIST,
                    CascadeType.REMOVE})
    List<Likes> Likes = new ArrayList<>();
}
