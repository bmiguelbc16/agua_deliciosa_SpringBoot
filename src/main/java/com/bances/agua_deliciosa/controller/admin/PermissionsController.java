package com.bances.agua_deliciosa.controller.admin;

import com.bances.agua_deliciosa.service.core.PermissionService;
import com.bances.agua_deliciosa.model.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping("/admin/permissions")
public class PermissionsController extends AdminController {

    private final PermissionService permissionService;

    // Inyección del servicio de permisos
    public PermissionsController(PermissionService permissionService) {
        super(null);  // Puedes inyectar el servicio de seguridad si es necesario
        this.permissionService = permissionService;
    }

    // Listar permisos
    @GetMapping
    public String listPermissions(Model model) {
        List<Permission> permissions = permissionService.listAll();
        model.addAttribute("permissions", permissions);
        setupCommonAttributes(model);  // Agregar atributos comunes
        return getAdminView("permissions/list");  // Vista para listar permisos
    }

    // Mostrar formulario para crear un nuevo permiso
    @GetMapping("/create")
    public String createPermissionForm(Model model) {
        setupCommonAttributes(model);  // Agregar atributos comunes
        return getAdminView("permissions/create");  // Vista para crear permiso
    }

    // Crear nuevo permiso
    @PostMapping("/create")
    public String createPermission(@RequestParam String name, @RequestParam String description,
                                    RedirectAttributes redirectAttributes) {
        try {
            permissionService.create(name, description);  // Crear permiso con nombre y descripción
            addSuccessMessage(redirectAttributes, "Permiso creado exitosamente");
        } catch (RuntimeException e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return redirect("/admin/permissions");
    }

    // Mostrar formulario para editar un permiso
    @GetMapping("/edit/{id}")
    public String editPermissionForm(@PathVariable Long id, Model model) {
        Permission permission = permissionService.getById(id);  // Obtener permiso por ID
        model.addAttribute("permission", permission);
        setupCommonAttributes(model);  // Agregar atributos comunes
        return getAdminView("permissions/edit");  // Vista para editar permiso
    }

    // Actualizar permiso
    @PostMapping("/edit/{id}")
    public String updatePermission(@PathVariable Long id, @RequestParam String name,
                                    @RequestParam String description, RedirectAttributes redirectAttributes) {
        try {
            permissionService.update(id, name, description);  // Actualizar permiso con nuevo nombre y descripción
            addSuccessMessage(redirectAttributes, "Permiso actualizado exitosamente");
        } catch (RuntimeException e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return redirect("/admin/permissions");
    }

    // Eliminar permiso
    @PostMapping("/delete/{id}")
    public String deletePermission(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            permissionService.delete(id);  // Eliminar permiso
            addSuccessMessage(redirectAttributes, "Permiso eliminado exitosamente");
        } catch (RuntimeException e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return redirect("/admin/permissions");
    }

    // Implementación del método getMenuActive() desde AdminController
    @Override
    protected String getMenuActive() {
        return "permissions";  // Indica que la sección de permisos está activa en el menú
    }
}
