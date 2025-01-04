package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.util.Routes;
import com.bances.agua_deliciosa.dto.admin.PermissionDTO;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.PermissionService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(Routes.Admin.PERMISSIONS)
public class PermissionsController extends AdminController {
    
    private final PermissionService permissionService;
    
    public PermissionsController(SecurityService securityService, PermissionService permissionService) {
        super(securityService);
        this.permissionService = permissionService;
    }

    @GetMapping
    public String index(Model model) {
        setupCommonAttributes(model, "permissions");
        model.addAttribute("title", "Gestión de Permisos");
        model.addAttribute("permissions", permissionService.getAllPermissions());
        return view("permissions/index");
    }

    @GetMapping("/create")
    public String create(Model model) {
        setupCommonAttributes(model, "permissions");
        model.addAttribute("title", "Nuevo Permiso");
        model.addAttribute("permission", new PermissionDTO());
        return view("permissions/create");
    }

    @PostMapping("/create")
    public String store(
        @Valid @ModelAttribute("permission") PermissionDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            permissionService.createPermission(dto);
            addSuccessMessage(redirectAttributes, "Permiso creado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al crear el permiso: " + e.getMessage());
            return redirect("create");
        }
        return redirect("");
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        setupCommonAttributes(model, "permissions");
        model.addAttribute("title", "Editar Permiso");
        model.addAttribute("permission", permissionService.getPermissionById(id));
        return view("permissions/edit");
    }

    @PostMapping("/{id}/update")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("permission") PermissionDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            permissionService.update(id, dto);
            addSuccessMessage(redirectAttributes, "Permiso actualizado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al actualizar el permiso: " + e.getMessage());
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
            permissionService.deletePermission(id);
            addSuccessMessage(redirectAttributes, "Permiso eliminado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al eliminar el permiso: " + e.getMessage());
        }
        return redirect("");
    }
}
