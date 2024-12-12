package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.dto.admin.EmployeeDTO;
import com.bances.agua_deliciosa.service.core.EmployeeService;
import com.bances.agua_deliciosa.service.core.RoleService;
import com.bances.agua_deliciosa.controller.base.BaseCrudController;
@Controller("adminEmployeeController")
@RequestMapping("/admin/employees")
public class EmployeeController extends BaseCrudController<Employee, EmployeeDTO> {
    
    private final RoleService roleService;
    
    public EmployeeController(
        EmployeeService employeeService,
        RoleService roleService
    ) {
        super(employeeService);
        this.roleService = roleService;
    }
    
    @Override
    protected void addCommonAttributes(Model model, HttpServletRequest request) {
        super.addCommonAttributes(model, request);
        model.addAttribute("roles", roleService.listAll());
    }
    
    @Override
    protected String getBasePath() {
        return "/admin/employees";
    }
    
    @Override
    protected String getViewPrefix() {
        return "admin/employees";
    }
    
    @Override
    protected String getEntityName() {
        return "Empleado";
    }
}