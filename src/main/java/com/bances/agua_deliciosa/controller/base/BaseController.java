package com.bances.agua_deliciosa.controller.base;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public abstract class BaseController {
    
    protected void addCommonAttributes(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getRequestURI());
        model.addAttribute("currentUser", getCurrentUser());
    }
    
    protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("success", message);
        log.info("Success: {}", message);
    }
    
    protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("error", message);
        log.error("Error: {}", message);
    }
    
    protected String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() ? auth.getName() : null;
    }
    
    protected String view(String path) {
        return getViewPrefix() + "/" + path;
    }
    
    protected String redirect(String path) {
        return "redirect:" + path;
    }
    
    protected abstract String getViewPrefix();
} 