package com.fourMan.GlobalAssets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceWithAssetDto {
    private List<PricesDto> pricesDtoList;
    private AssetsDto assetsDto;
}
