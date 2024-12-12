package com.bances.agua_deliciosa.controller.client;

import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PreAuthorize("hasRole('CLIENTE')")
public abstract class ClientController extends BaseController {
    
    protected final SecurityService securityService;
    
    protected ClientController(SecurityService securityService) {
        this.securityService = securityService;
    }
    
    @Override
    protected String getViewPrefix() {
        return "client";
    }
    
    protected void setupCommonAttributes(Model model) {
        model.addAttribute("currentUser", securityService.getCurrentUser());
        model.addAttribute("isClient", true);
        model.addAttribute("menuActive", getMenuActive());
    }
    
    protected String getClientView(String viewName) {
        return getViewPrefix() + "/" + viewName;
    }
    
    protected abstract String getMenuActive();
} 