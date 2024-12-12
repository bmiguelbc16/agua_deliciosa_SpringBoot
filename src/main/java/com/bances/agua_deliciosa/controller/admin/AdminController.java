package com.bances.agua_deliciosa.controller.admin;

import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public abstract class AdminController extends BaseController {
    
    protected final SecurityService securityService;
    
    protected AdminController(SecurityService securityService) {
        this.securityService = securityService;
    }
    
    @Override
    protected String getViewPrefix() {
        return "admin";
    }
    
    protected void setupCommonAttributes(Model model) {
        model.addAttribute("currentUser", securityService.getCurrentUser());
        model.addAttribute("isAdmin", true);
        model.addAttribute("menuActive", getMenuActive());
    }
    
    protected String getAdminView(String viewName) {
        return getViewPrefix() + "/" + viewName;
    }
    
    protected abstract String getMenuActive();
} 