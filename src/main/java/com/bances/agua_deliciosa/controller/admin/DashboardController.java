package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bances.agua_deliciosa.service.core.DashboardService;
import com.bances.agua_deliciosa.service.auth.SecurityService;

@Controller
@RequestMapping("/admin")
public class DashboardController extends AdminController {
    
    private final DashboardService dashboardService;
    
    public DashboardController(SecurityService securityService, DashboardService dashboardService) {
        super(securityService);
        this.dashboardService = dashboardService;
    }
    
    @GetMapping({"/", "/dashboard"})
    public String index(Model model) {
        setupCommonAttributes(model);
        model.addAttribute("title", "Dashboard | Agua Deliciosa");
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("view", "/admin/dashboard/index");
        return "admin/layout/main";
    }
    
    @Override
    protected String getMenuActive() {
        return "dashboard";
    }
} 