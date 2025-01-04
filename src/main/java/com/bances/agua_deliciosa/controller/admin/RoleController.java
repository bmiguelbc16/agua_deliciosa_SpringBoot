package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.util.Routes;
import com.bances.agua_deliciosa.controller.base.BaseController;
import com.bances.agua_deliciosa.dto.admin.RoleDTO;
import com.bances.agua_deliciosa.service.core.RoleService;
import com.bances.agua_deliciosa.service.core.PermissionService;
import com.bances.agua_deliciosa.service.auth.SecurityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(Routes.Admin.ROLES)
@RequiredArgsConstructor
public class RoleController extends BaseController {

    private final SecurityService securityService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Override
    protected String getViewPrefix() {
        return "admin";
    }

    private void setupCommonAttributes(Model model) {
        model.addAttribute("currentUser", securityService.getCurrentUser());
        model.addAttribute("isAdmin", true);
        model.addAttribute("menuActive", "roles");
    }

    @GetMapping
    public String index(Model model) {
        setupCommonAttributes(model);
        model.addAttribute("title", "Gestión de Roles");
        model.addAttribute("roles", roleService.listAll());
        return view("roles/index");
    }

    @GetMapping("/create")
    public String create(Model model) {
        setupCommonAttributes(model);
        model.addAttribute("title", "Nuevo Rol");
        model.addAttribute("permissions", permissionService.getAllPermissions());
        model.addAttribute("role", new RoleDTO());
        return view("roles/create");
    }

    @PostMapping("/create")
    public String store(
        @Valid @ModelAttribute("role") RoleDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            roleService.create(dto);
            addSuccessMessage(redirectAttributes, "Rol creado exitosamente");
        } catch (RuntimeException e) {
            addErrorMessage(redirectAttributes, "Error al crear el rol: " + e.getMessage());
            return redirect("create");
        }
        return redirect("");
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        setupCommonAttributes(model);
        model.addAttribute("title", "Editar Rol");
        model.addAttribute("role", roleService.getById(id));
        model.addAttribute("permissions", permissionService.getAllPermissions());
        return view("roles/edit");
    }

    @PostMapping("/{id}/update")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("role") RoleDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            roleService.update(id, dto);
            addSuccessMessage(redirectAttributes, "Rol actualizado exitosamente");
        } catch (RuntimeException e) {
            addErrorMessage(redirectAttributes, "Error al actualizar el rol: " + e.getMessage());
            return redirect(id + "/edit");
        }
        return redirect("");
    }

    @PostMapping("/{id}/delete")
    public String delete(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes
    ) {
        try {
            roleService.delete(id);
            addSuccessMessage(redirectAttributes, "Rol eliminado exitosamente");
        } catch (RuntimeException e) {
            addErrorMessage(redirectAttributes, "Error al eliminar el rol: " + e.getMessage());
        }
        return redirect("");
    }
}
