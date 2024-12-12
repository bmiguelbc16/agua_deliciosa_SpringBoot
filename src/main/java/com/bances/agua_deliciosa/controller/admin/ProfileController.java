package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import com.bances.agua_deliciosa.dto.admin.ProfileDTO;
import com.bances.agua_deliciosa.service.core.ProfileService;
import com.bances.agua_deliciosa.service.auth.SecurityService;

@Controller("adminProfileController")
@RequestMapping("/admin/profile")
public class ProfileController extends AdminController {
    
    private final ProfileService profileService;
    
    public ProfileController(
        SecurityService securityService,
        ProfileService profileService
    ) {
        super(securityService);
        this.profileService = profileService;
    }
    
    @GetMapping
    public String show(Model model) {
        setupCommonAttributes(model);
        model.addAttribute("pageTitle", "Mi Perfil");
        model.addAttribute("profile", profileService.getCurrentUserProfile());
        return getAdminView("profile/form");
    }
    
    @PostMapping("/update")
    public String update(
        @Valid @ModelAttribute("profile") ProfileDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            profileService.updateProfile(dto);
            addSuccessMessage(redirectAttributes, "Perfil actualizado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return "redirect:/admin/profile";
    }
    
    @Override
    protected String getMenuActive() {
        return "profile";
    }
} 