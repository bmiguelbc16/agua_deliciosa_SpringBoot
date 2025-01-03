package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bances.agua_deliciosa.service.core.RoleService;

@Controller
@RequestMapping("/admin/roles")
public class RoleController {
    
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("view", "admin/roles/index");
        model.addAttribute("title", "Roles | Agua Deliciosa");
        return "admin/layout/main";
    }
}
