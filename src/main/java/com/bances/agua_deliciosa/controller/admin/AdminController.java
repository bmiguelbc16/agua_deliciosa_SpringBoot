package com.bances.agua_deliciosa.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import com.bances.agua_deliciosa.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public abstract class AdminController extends BaseController {
    // Funcionalidad común para todos los controladores de admin
    @Override
    protected void addCommonAttributes(Model model, HttpServletRequest request) {
        super.addCommonAttributes(model, request);
        model.addAttribute("currentUrl", request.getRequestURI());
    }
    
    protected void logAdminAction(String action) {
        log.info("Admin action: {}", action);
    }
    
    @ModelAttribute("currentUrl")
    public String getCurrentUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }
} 