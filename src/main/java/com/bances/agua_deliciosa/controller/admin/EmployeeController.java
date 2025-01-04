package com.bances.agua_deliciosa.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.util.Routes;
import com.bances.agua_deliciosa.dto.admin.EmployeeDTO;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(Routes.Admin.EMPLOYEES)
public class EmployeeController extends AdminController {
    
    private final EmployeeService employeeService;
    
    public EmployeeController(SecurityService securityService, EmployeeService employeeService) {
        super(securityService);
        this.employeeService = employeeService;
    }

    @GetMapping
    public String index(
        @RequestParam(required = false) String search,
        @PageableDefault(size = 10) Pageable pageable,
        Model model
    ) {
        setupCommonAttributes(model, "employees");
        model.addAttribute("title", "Gestión de Empleados");
        model.addAttribute("employees", employeeService.getEmployeesPage(pageable));
        return view("employees/index");
    }

    @GetMapping("/create")
    public String create(Model model) {
        setupCommonAttributes(model, "employees");
        model.addAttribute("title", "Nuevo Empleado");
        model.addAttribute("employee", new EmployeeDTO());
        return view("employees/create");
    }

    @PostMapping("/create")
    public String store(
        @Valid @ModelAttribute("employee") EmployeeDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            employeeService.createEmployee(dto);
            addSuccessMessage(redirectAttributes, "Empleado creado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al crear el empleado: " + e.getMessage());
            return redirect("create");
        }
        return redirect("");
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        setupCommonAttributes(model, "employees");
        model.addAttribute("title", "Editar Empleado");
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return view("employees/edit");
    }

    @PostMapping("/{id}/update")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("employee") EmployeeDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            employeeService.update(id, dto);
            addSuccessMessage(redirectAttributes, "Empleado actualizado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al actualizar el empleado: " + e.getMessage());
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
            employeeService.deleteEmployee(id);
            addSuccessMessage(redirectAttributes, "Empleado eliminado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al eliminar el empleado: " + e.getMessage());
        }
        return redirect("");
    }
}
