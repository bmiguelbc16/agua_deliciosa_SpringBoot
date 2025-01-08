package com.bances.agua_deliciosa.controller.admin;

import com.bances.agua_deliciosa.model.Permission;
import com.bances.agua_deliciosa.service.core.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/permissions")
@RequiredArgsConstructor
public class PermissionsController {
    private final PermissionService permissionService;

    protected String view(String viewName) {
        return "admin/permissions/" + viewName;
    }

    protected String redirect(String route) {
        return "redirect:" + route;
    }

    protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("success", message);
    }

    protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("error", message);
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        return view("index");
    }

    @GetMapping("/create")
    public String create(Model model) {
        Permission permission = new Permission();
        permission.setRoleId(1L); // Default role ID
        model.addAttribute("permission", permission);
        return view("form");
    }

    @PostMapping
    public String store(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Long roleId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Permission permission = new Permission();
            permission.setName(name);
            permission.setDescription(description);
            permission.setRoleId(roleId);
            
            permissionService.save(permission);
            addSuccessMessage(redirectAttributes, "Permission created successfully");
            return redirect("/admin/permissions");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/admin/permissions/create");
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Permission permission = permissionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        model.addAttribute("permission", permission);
        return view("form");
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Long roleId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Permission permission = permissionService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Permission not found"));
            permission.setName(name);
            permission.setDescription(description);
            permission.setRoleId(roleId);
            
            permissionService.save(permission);
            addSuccessMessage(redirectAttributes, "Permission updated successfully");
            return redirect("/admin/permissions");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/admin/permissions/" + id + "/edit");
        }
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Permission permission = permissionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        model.addAttribute("permission", permission);
        return view("show");
    }

    @DeleteMapping("/{id}")
    public String destroy(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            permissionService.deleteById(id);
            addSuccessMessage(redirectAttributes, "Permission deleted successfully");
            return redirect("/admin/permissions");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/admin/permissions");
        }
    }
}
