package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//광고 엔티티
public class Advertisement {
    //광고 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adventId;

    //광고 순번
    private Long sortNum;

    //광고 이미지 주소
    private String imagePath;
}
