package com.fourMan.GlobalAssets.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asset")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AssetsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 심볼/코드 중 하나는 유니크하게 사용 */
    @Column(nullable = false, unique = true, length = 60)
    private String code;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 60)
    private String symbol;

    /** 자산 카테고리(FX 등). JPA 상속 안 씁니다. */
    @Column(length = 30)
    private String category;
}
