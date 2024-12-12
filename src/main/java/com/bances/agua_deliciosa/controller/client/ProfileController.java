package com.bances.agua_deliciosa.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.ProfileService;
import com.bances.agua_deliciosa.dto.client.ClientProfileDTO;
import jakarta.validation.Valid;

@Controller("clientProfileController")
@RequestMapping("/client/profile")
public class ProfileController extends ClientController {
    
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
        model.addAttribute("profile", profileService.getCurrentClientProfile());
        return getClientView("profile/show");
    }
    
    @PostMapping("/update")
    public String update(
        @Valid @ModelAttribute ClientProfileDTO profileDTO,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return getClientView("profile/show");
        }
        
        try {
            profileService.updateClientProfile(profileDTO);
            addSuccessMessage(redirectAttributes, "Perfil actualizado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return "redirect:/client/profile";
    }
    
    @Override
    protected String getMenuActive() {
        return "profile";
    }
} 