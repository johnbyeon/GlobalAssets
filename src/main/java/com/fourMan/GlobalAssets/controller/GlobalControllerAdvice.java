package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.service.AdvertisementService;
import com.fourMan.GlobalAssets.dto.AdvertisementDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final AdvertisementService advertisementService;

    public GlobalControllerAdvice(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @ModelAttribute("dtoList")
    public List<AdvertisementDto> addAdvertisementsToModel() {
        return advertisementService.getAdverisementList();
    }
}
