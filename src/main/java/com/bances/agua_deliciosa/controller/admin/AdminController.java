package com.bances.agua_deliciosa.controller.admin;

import org.springframework.ui.Model;
import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.service.auth.SecurityService;

public abstract class AdminController extends BaseController {
    
    protected final SecurityService securityService;

    protected AdminController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected String getViewPrefix() {
        return "admin";
    }

    protected void setupCommonAttributes(Model model, String menuActive) {
        model.addAttribute("currentUser", securityService.getUser());
        model.addAttribute("isAdmin", true);
        model.addAttribute("menuActive", menuActive);
    }
}