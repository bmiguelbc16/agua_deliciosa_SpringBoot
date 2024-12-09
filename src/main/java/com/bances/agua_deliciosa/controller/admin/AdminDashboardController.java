package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminDashboardController extends AdminController {
    
    @GetMapping("/dashboard")
    public String index(Model model) {
        logAdminAction("Accessing dashboard");
        return "admin/dashboard";
    }
} 