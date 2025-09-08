package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping({"/",""})
    public String GotoMain(){
        return "main";
    }

    @GetMapping("login")
    public String GotoLogin(Model model){
        UserDto dto = new UserDto();
        model.addAttribute("dto", dto);
        return "login";
    }

    @GetMapping("signup")
    public String GotoSignup(Model model){
        UserDto dto = new UserDto();
        model.addAttribute("dto", dto);
        return "signup";
    }
}
