package com.bances.agua_deliciosa.controller.admin;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasRole('Admin')")
public class EmployeeController extends AdminController {

    private final EmployeeService employeeService;

    public EmployeeController(SecurityService securityService, EmployeeService employeeService) {
        super(securityService);
        this.employeeService = employeeService;
    }

    @GetMapping
    public String index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        setupCommonAttributes(model, "employees");
        
        Pageable paging = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Employee> employeePage = employeeService.getEmployeesPage(paging);

        model.addAttribute("employees", employeePage);
        model.addAttribute("currentPage", employeePage.getNumber());
        model.addAttribute("totalItems", employeePage.getTotalElements());
        model.addAttribute("totalPages", employeePage.getTotalPages());

        return "admin/employees/index";
    }

    @GetMapping("/api")
    @ResponseBody
    public Map<String, Object> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable paging = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Employee> employeePage = employeeService.getEmployeesPage(paging);

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
