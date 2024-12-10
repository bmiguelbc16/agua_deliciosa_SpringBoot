package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.dto.EmployeeDTO;
import com.bances.agua_deliciosa.service.EmployeeService;
import com.bances.agua_deliciosa.service.RoleService;
import com.bances.agua_deliciosa.model.Employee;

import java.util.Map;

@Controller
@RequestMapping("/admin/seguridad/trabajadores")
public class EmployeeController extends AdminController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping
    public String index(Model model, 
                    HttpServletRequest request,
                    @RequestParam(required = false) String search,
                    @RequestParam(defaultValue = "0") int page) {
        Page<Employee> employees = employeeService.findAll(search, PageRequest.of(page, 10));
        model.addAttribute("employees", employees);
        addCommonAttributes(model, request);
        return "admin/employees/index";
    }
    
    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setActive(true); // Valor por defecto
        
        model.addAttribute("employee", employeeDTO);
        model.addAttribute("roles", roleService.getAvailableRoles());
        addCommonAttributes(model, request);
        return "admin/employees/create";
    }
    
    @PostMapping
    public String store(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
                        BindingResult result,
                        Model model,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {
        // Validar que las contraseñas coincidan
        if (!employeeDTO.getPassword().equals(employeeDTO.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Las contraseñas no coinciden");
        }

        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.getAvailableRoles());
            addCommonAttributes(model, request);
            return "admin/employees/create";
        }
        
        try {
            employeeService.create(employeeDTO);
            redirectAttributes.addFlashAttribute("success", "Empleado creado correctamente");
            return "redirect:/admin/seguridad/trabajadores";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.getAvailableRoles());
            addCommonAttributes(model, request);
            return "admin/employees/create";
        }
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, HttpServletRequest request) {
        Employee employee = employeeService.findById(id);
        
        // Crear DTO con datos del empleado
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(employee.getUser().getName());
        employeeDTO.setLastName(employee.getUser().getLastName());
        employeeDTO.setDocumentNumber(employee.getUser().getDocumentNumber());
        employeeDTO.setEmail(employee.getUser().getEmail());
        employeeDTO.setPhoneNumber(employee.getUser().getPhoneNumber());
        employeeDTO.setGender(employee.getUser().getGender());
        employeeDTO.setBirthDate(employee.getUser().getBirthDate());
        
        // Obtener el rol actual sin el prefijo ROLE_
        String currentRole = employee.getUser().getRoles().stream()
            .findFirst()
            .map(role -> role.getName().replace("ROLE_", ""))
            .orElse("");
        employeeDTO.setRole(currentRole);
        employeeDTO.setActive(employee.getUser().isActive());
        
        model.addAttribute("employee", employeeDTO);
        model.addAttribute("employeeId", id);
        model.addAttribute("roles", roleService.getAvailableRoles());
        addCommonAttributes(model, request);
        return "admin/employees/edit";
    }
    
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, 
                        @Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
                        BindingResult result,
                        Model model,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.getAvailableRoles());
            addCommonAttributes(model, request);
            return "admin/employees/edit";
        }
        
        try {
            employeeService.update(id, employeeDTO);
            redirectAttributes.addFlashAttribute("success", "Empleado actualizado correctamente");
            return "redirect:/admin/seguridad/trabajadores";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.getAvailableRoles());
            addCommonAttributes(model, request);
            return "admin/employees/edit";
        }
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> destroy(@PathVariable Long id) {
        try {
            employeeService.delete(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empleado eliminado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Error al eliminar empleado: " + e.getMessage()
            ));
        }
    }
} 