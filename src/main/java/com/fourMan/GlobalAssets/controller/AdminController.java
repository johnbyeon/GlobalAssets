package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.AdvertisementDto;
import com.fourMan.GlobalAssets.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdvertisementService advertisementService;

    @GetMapping("/get/userList")
    public String getUserList(Model model) {
        return "userList :: userTable"; // fragment 이름 확인
    }

    @GetMapping("/get/boardList")
    public String getBoardList(Model model) {
        return "boardList :: boardTable";
    }

    @GetMapping("/get/imgUp")
    public String getImgUp() {
        return "imgUp :: imgUploadForm";
    }

    @GetMapping("/get/adv")
    public String getAdv(Model model) {
        List<AdvertisementDto> advertisementDtoList = advertisementService.getAdverisementList();
        model.addAttribute("dtoList", advertisementDtoList);
        return "advList :: advManage";
    }

    // 광고 순서 저장
    @PostMapping("/admin/advertisement/order")
    public ResponseEntity<?> saveAdOrder(@RequestBody List<AdvertisementDto> orderList){
        try {
            advertisementService.updateAdvertisementOrder(orderList);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("광고 순서 저장 중 오류: " + e.getMessage());
        }
    }
}
