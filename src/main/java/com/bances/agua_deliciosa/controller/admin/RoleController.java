package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.util.Routes;
import com.bances.agua_deliciosa.dto.admin.RoleDTO;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.RoleService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(Routes.Admin.ROLES)
public class RoleController extends AdminController {
    
    private final RoleService roleService;
    
    public RoleController(SecurityService securityService, RoleService roleService) {
        super(securityService);
        this.roleService = roleService;
    }

    @GetMapping
    public String index(Model model) {
        setupCommonAttributes(model, "roles");
        model.addAttribute("title", "Gestión de Roles");
        model.addAttribute("roles", roleService.listAll());
        return view("roles/index");
    }

    @GetMapping("/create")
    public String create(Model model) {
        setupCommonAttributes(model, "roles");
        model.addAttribute("title", "Nuevo Rol");
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
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al crear el rol: " + e.getMessage());
            return redirect("create");
        }
        return redirect("");
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        setupCommonAttributes(model, "roles");
        model.addAttribute("title", "Editar Rol");
        model.addAttribute("role", roleService.getById(id));
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al eliminar el rol: " + e.getMessage());
        }
        return redirect("");
    }
}
