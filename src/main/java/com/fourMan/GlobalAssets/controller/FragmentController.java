package com.fourMan.GlobalAssets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FragmentController {
    @GetMapping("/fragments/header")
    public String getHeader() {
        return "fragments/header"; // fragment 반환
    }

    @GetMapping("/fragments/footer")
    public String getFooter() {
        return "fragments/footer";
    }
}
