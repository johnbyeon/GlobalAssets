package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.LoginDto;
import com.fourMan.GlobalAssets.dto.SignupDto;
import com.fourMan.GlobalAssets.dto.UserDto;
import com.fourMan.GlobalAssets.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("login")
    public String GotoLogin(Model model){
        LoginDto dto = new LoginDto();
        model.addAttribute("dto", dto);
        return "login";
    }


}
