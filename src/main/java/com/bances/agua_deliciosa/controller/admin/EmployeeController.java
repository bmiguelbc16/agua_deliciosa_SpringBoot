package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.bances.agua_deliciosa.dto.EmployeeDTO;
import com.bances.agua_deliciosa.service.EmployeeService;
import com.bances.agua_deliciosa.service.RoleService;
import com.bances.agua_deliciosa.model.Employee;

import java.util.Map;

@Controller
@RequestMapping("/admin/employees")
@Slf4j
public class EmployeeController extends AdminController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping
    public String index(Model model, 
                        @RequestParam(required = false) String search,
                        @RequestParam(defaultValue = "0") int page) {
        Page<Employee> employees = employeeService.findAll(search, PageRequest.of(page, 10));
        model.addAttribute("employees", employees);
        return "admin/employees/index";
    }
    
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        model.addAttribute("genderOptions", Employee.OPTIONS_GENDER);
        model.addAttribute("roles", roleService.getAvailableRoles());
        return "admin/employees/create";
    }
    
    @PostMapping
    public String store(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
                        BindingResult result,
                        Model model) {
        logAdminAction("Creating new employee");
        
        if (result.hasErrors()) {
            model.addAttribute("genderOptions", Employee.OPTIONS_GENDER);
            return "admin/employees/create";
        }
        
        try {
            employeeService.create(employeeDTO);
            return "redirect:/admin/employees?success=Empleado creado correctamente";
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear empleado: " + e.getMessage());
            return "admin/employees/create";
        }
    }
    
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("genderOptions", Employee.OPTIONS_GENDER);
        
        return "admin/employees/edit";
    }
    
    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                        @Valid @ModelAttribute("employee") EmployeeDTO employeeDTO,
                        BindingResult result,
                        Model model) {
        logAdminAction("Updating employee");
        
        if (result.hasErrors()) {
            model.addAttribute("genderOptions", Employee.OPTIONS_GENDER);
            return "admin/employees/edit";
        }
        
        try {
            employeeService.update(id, employeeDTO);
            return "redirect:/admin/employees?success=Empleado actualizado correctamente";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar empleado: " + e.getMessage());
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