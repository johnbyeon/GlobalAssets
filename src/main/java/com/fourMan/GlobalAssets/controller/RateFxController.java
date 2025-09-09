// src/main/java/com/fourMan/GlobalAssets/controller/RateFxController.java
package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.service.RateFxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RateFxController {

    private final RateFxService rateFxService;

    @GetMapping("/rate/admin/refresh")
    public String refresh() {
        rateFxService.refreshAllFromApi();
        return "redirect:/rate/fx/daily";
    }

    @GetMapping("/rate/fx/daily")
    public String daily(Model model) {
        model.addAttribute("usdRows", rateFxService.loadRowsFor("USD/KRW", 10));
        model.addAttribute("jpyRows", rateFxService.loadRowsFor("JPY/KRW", 10));
        model.addAttribute("eurRows", rateFxService.loadRowsFor("EUR/KRW", 10));
        return "rate_fx_daily";
    }
}
