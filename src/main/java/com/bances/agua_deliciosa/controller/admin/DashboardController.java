package com.bances.agua_deliciosa.controller.admin;

import com.bances.agua_deliciosa.service.core.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    protected String view(String viewName) {
        return "admin/dashboard/" + viewName;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        return view("index");
    }
}