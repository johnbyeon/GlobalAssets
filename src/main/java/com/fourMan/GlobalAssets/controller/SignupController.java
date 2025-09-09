package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.SignupDto;
import com.fourMan.GlobalAssets.dto.UserDto;
import com.fourMan.GlobalAssets.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
@Slf4j
@Controller
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;

    @GetMapping("signup")
    public String GotoSignup(Model model){
        SignupDto signupDto = new SignupDto();
        model.addAttribute("dto", signupDto);
        return "signup";
    }

    @PostMapping("/signupProc")
    public String joinProcess(@ModelAttribute("dto")SignupDto signupDto) {
        log.info("회원가입 진입확인");
        signupDto.setEmail(signupDto.getEmail().toLowerCase());
        signupService.SignupProcess(signupDto);
        return "redirect:/login";
    }

}


