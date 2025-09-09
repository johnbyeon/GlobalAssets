package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssetsEntity {
    //종목번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetId;
    //종목코드
    @Column(nullable = false)
    private String symbol;
    //종목이름
    @Column(nullable = false)
    private String name;
}
