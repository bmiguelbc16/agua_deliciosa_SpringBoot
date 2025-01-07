package com.bances.agua_deliciosa.controller.admin;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bances.agua_deliciosa.dto.admin.EmployeeDTO;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/employees")
public class EmployeeController extends AdminController {

    private final EmployeeService employeeService;

    public EmployeeController(SecurityService securityService, EmployeeService employeeService) {
        super(securityService);
        this.employeeService = employeeService;
    }

    @GetMapping
    public String index(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<Employee> employeePage = employeeService.getEmployees(page, size, null);
        model.addAttribute("employees", employeePage);
        return "admin/employees/index";
    }

    @GetMapping("/api")
    @ResponseBody
    public Map<String, Object> getEmployees(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Employee> employeePage = employeeService.getEmployees(page, size, null);
        Map<String, Object> response = new HashMap<>();
        response.put("employees", employeePage.getContent());
        response.put("currentPage", employeePage.getNumber());
        response.put("totalItems", employeePage.getTotalElements());
        response.put("totalPages", employeePage.getTotalPages());
        return response;
    }

    @PostMapping
    @ResponseBody
    public Employee create(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.createEmployee(employeeDTO);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Employee update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateEmployee(id, employeeDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}
