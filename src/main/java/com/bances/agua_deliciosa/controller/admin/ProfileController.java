package com.bances.agua_deliciosa.controller.admin;

import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final SecurityService securityService;
    private final UserService userService;

    protected String view(String viewName) {
        return "admin/profile/" + viewName;
    }

    protected String redirect(String path) {
        return "redirect:" + path;
    }

    protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("successMessage", message);
    }

    protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("errorMessage", message);
    }

    @GetMapping
    public String show(Model model) {
        User currentUser = securityService.getUser();
        model.addAttribute("user", currentUser);
        return view("show");
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        User currentUser = securityService.getUser();
        model.addAttribute("user", currentUser);
        return view("edit");
    }

    @PostMapping
    public String update(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String password,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User currentUser = securityService.getUser();
            currentUser.setName(name);
            currentUser.setEmail(email);
            
            if (password != null && !password.isEmpty()) {
                userService.updatePassword(currentUser, password);
            }
            
            userService.save(currentUser);
            addSuccessMessage(redirectAttributes, "Perfil actualizado exitosamente");
            return redirect("/admin/profile");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/admin/profile/edit");
        }
    }
}