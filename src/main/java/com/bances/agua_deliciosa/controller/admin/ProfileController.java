package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.util.Routes;
import com.bances.agua_deliciosa.dto.admin.ProfileDTO;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(Routes.Admin.PROFILE)
public class ProfileController extends AdminController {
    
    private final UserService userService;
    
    public ProfileController(SecurityService securityService, UserService userService) {
        super(securityService);
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model) {
        setupCommonAttributes(model, "profile");
        model.addAttribute("title", "Mi Perfil");
        model.addAttribute("user", userService.getCurrentUserProfile());
        return view("profile/index");
    }

    @PostMapping("/update")
    public String update(
        @Valid @ModelAttribute("user") ProfileDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            userService.updateProfile(dto);
            addSuccessMessage(redirectAttributes, "Perfil actualizado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al actualizar el perfil: " + e.getMessage());
        }
        return redirect("");
    }
}