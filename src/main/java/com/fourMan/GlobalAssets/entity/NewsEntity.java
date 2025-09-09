package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class NewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;
    //종목번호 어떤종목의 뉴스인가??
    private Long assetId;
    //뉴스날짜
    private Timestamp timestamp;

    private String title;

    private String urlLink;
}
