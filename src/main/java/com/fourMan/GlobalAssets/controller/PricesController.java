package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.config.ADMIN;
import com.fourMan.GlobalAssets.dto.AssetsDto;
import com.fourMan.GlobalAssets.dto.PriceWithAssetDto;
import com.fourMan.GlobalAssets.dto.PricesDto;
import com.fourMan.GlobalAssets.service.AssetService;
import com.fourMan.GlobalAssets.service.PricesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PricesController {
    private final PricesService pricesService;
    private final AssetService assetService;

    @GetMapping("/chart/{assetname}")
    @ResponseBody
    public PriceWithAssetDto getChartData(@PathVariable("assetname") String assetname) {
        assetname = assetname.toUpperCase();
        log.info("assetname data : {}", assetname);
        PriceWithAssetDto priceWithAssetDto = new PriceWithAssetDto();
        for (int i = 0; i < ADMIN.INIT_CRYPTO.EN_SYMBOLS.length; i++) {
            if (assetname.equals(ADMIN.INIT_CRYPTO.EN_SYMBOLS[i])) {
                String simbol = ADMIN.INIT_CRYPTO.SYMBOLS[i];
                log.info("Simbol data : {}", simbol);

                AssetsDto dto = assetService.findOneSymbol(simbol);
                log.info("AssetsDto data : {}", dto);
                priceWithAssetDto.setAssetsDto(dto);
                if (!ObjectUtils.isEmpty(dto)) {
                    List<PricesDto> dtoList = pricesService.findTop20ByAssetIdOrderByTimestampDesc(dto.getId());
                    dtoList = dtoList.stream()
                            .sorted(Comparator.comparing(PricesDto::getPriceId))
                            .collect(Collectors.toList());
                    //Collections.reverse(dtoList);
                    log.info("DTO내의 data : {}", dtoList);
                    priceWithAssetDto.setPricesDtoList(dtoList);
                    return priceWithAssetDto;  // JSON으로 자동 변환됨
                }
            }
        }
        return priceWithAssetDto;
    }
    @GetMapping("/chart-page/{assetname}")
    public String chartPage(@PathVariable String assetname, Model model) {
        model.addAttribute("assetname", assetname.toUpperCase());
        return "charts"; // 위 Thymeleaf 템플릿 파일명
    }
}
