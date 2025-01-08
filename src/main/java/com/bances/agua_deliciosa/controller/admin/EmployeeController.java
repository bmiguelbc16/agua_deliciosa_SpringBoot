package com.bances.agua_deliciosa.controller.admin;

import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.service.core.RoleService;
import com.bances.agua_deliciosa.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final UserService userService;
    private final RoleService roleService;

    protected String view(String viewName) {
        return "admin/employees/" + viewName;
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
        model.addAttribute("employees", userService.findAllEmployees());
        return view("index");
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("employee", new User());
        model.addAttribute("roles", roleService.findEmployeeRoles());
        return view("form");
    }

    @PostMapping
    public String store(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Long roleId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User employee = new User();
            employee.setName(name);
            employee.setEmail(email);
            
            Role role = roleService.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            employee.setRoleId(role.getId());
            
            userService.createUser(employee, password);
            addSuccessMessage(redirectAttributes, "Empleado creado exitosamente");
            return redirect("/admin/employees");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/admin/employees/create");
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        User employee = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        model.addAttribute("employee", employee);
        model.addAttribute("roles", roleService.findEmployeeRoles());
        return view("form");
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam Long roleId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User employee = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            Role role = roleService.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            
            employee.setName(name);
            employee.setEmail(email);
            employee.setRoleId(role.getId());
            
            userService.save(employee);
            addSuccessMessage(redirectAttributes, "Employee updated successfully");
            return "redirect:/admin/employees";
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error updating employee: " + e.getMessage());
            return "redirect:/admin/employees/" + id + "/edit";
        }
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        User employee = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Role role = roleService.findById(employee.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        model.addAttribute("employee", employee);
        model.addAttribute("role", role);
        return view("show");
    }

    @DeleteMapping("/{id}")
    public String destroy(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.deleteById(id);
            addSuccessMessage(redirectAttributes, "Empleado eliminado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return redirect("/admin/employees");
    }
}
