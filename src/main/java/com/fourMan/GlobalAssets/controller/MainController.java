package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.UserDto;
import com.fourMan.GlobalAssets.service.ApprovalKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ApprovalKeyService approvalKeyService;

    @GetMapping({"/",""})
    public String GotoMain(){
        return "index";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/exchange")
    public String userOnlyExchange() {
        return "exchange";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/stockCoin")
    public String userOnlyStockCoin() {return "stockCoin";}

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/admin")
    public String adminOnlyAdminPage() {
        return "admin";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/adminOnly")
    public String adminOnly() {
        return "관리자 전용 페이지!";
    }


    @GetMapping("/get/key")
    public String Getkey(Model model){
        String approvalKey = approvalKeyService.getApprovalKey();
        System.out.println("Approval Key: " + approvalKey);
        model.addAttribute("key",approvalKey);
        return  "getkey";
    }

}
