package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.DashboardService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class DashboardController extends AdminController {
    
    private final DashboardService dashboardService;
    
    public DashboardController(SecurityService securityService, DashboardService dashboardService) {
        super(securityService);
        this.dashboardService = dashboardService;
    }
    
    @GetMapping({"/", "/dashboard"})  // Maneja tanto /admin como /admin/dashboard
    public String index(Model model) {
        log.info("Accediendo al dashboard de admin");
        setupCommonAttributes(model, "dashboard");
        model.addAttribute("title", "Dashboard | Agua Deliciosa");
        
        var dashboardData = dashboardService.getDashboardData();
        model.addAttribute("stats", dashboardData.getStats());
        model.addAttribute("recentOrders", dashboardData.getRecentOrders());
        model.addAttribute("topProducts", dashboardData.getTopProducts());
        model.addAttribute("topCustomers", dashboardData.getTopCustomers());
        
        return "admin/dashboard/index";
    }
}