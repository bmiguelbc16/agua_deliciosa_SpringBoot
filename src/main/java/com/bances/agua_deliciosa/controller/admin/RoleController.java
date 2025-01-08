package com.bances.agua_deliciosa.controller.admin;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.service.core.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    protected String view(String viewName) {
        return "admin/roles/" + viewName;
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
    public String index(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return view("index");
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("role", new Role());
        return view("form");
    }

    @PostMapping
    public String store(
            @RequestParam String name,
            @RequestParam String description,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleService.save(role);
            addSuccessMessage(redirectAttributes, "Rol creado exitosamente");
            return redirect("/admin/roles");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/admin/roles/create");
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Role role = roleService.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        model.addAttribute("role", role);
        return "admin/roles/edit";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Role role = roleService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            role.setName(name);
            role.setDescription(description);
            
            roleService.save(role);
            addSuccessMessage(redirectAttributes, "Role updated successfully");
            return "redirect:/admin/roles";
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error updating role: " + e.getMessage());
            return "redirect:/admin/roles/" + id + "/edit";
        }
    }

    @DeleteMapping("/{id}")
    public String destroy(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            roleService.deleteById(id);
            addSuccessMessage(redirectAttributes, "Rol eliminado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return redirect("/admin/roles");
    }
}
