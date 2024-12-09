package com.bances.agua_deliciosa.controller.client;

import org.springframework.stereotype.Controller;
import com.bances.agua_deliciosa.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientDashboardController extends BaseController {
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "client/dashboard";
    }
} 
