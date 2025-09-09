
package com.fourMan.GlobalAssets.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RateAsset {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long assetId;

    @Column(nullable = false, unique = true)
    private String symbol;

    private String symbolAlt;

    @Column(nullable = false)
    private String name;
}
