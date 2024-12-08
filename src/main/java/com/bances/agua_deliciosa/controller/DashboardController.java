package com.bances.agua_deliciosa.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DashboardController {

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        log.info("Accediendo al dashboard de admin");
        return "admin/dashboard";
    }

    @GetMapping("/gerente/dashboard")
    @PreAuthorize("hasRole('GERENTE')")
    public String gerenteDashboard() {
        return "gerente/dashboard";
    }

    @GetMapping("/vendedor/dashboard")
    @PreAuthorize("hasRole('VENDEDOR')")
    public String vendedorDashboard() {
        return "vendedor/dashboard";
    }

    @GetMapping("/repartidor/dashboard")
    @PreAuthorize("hasRole('REPARTIDOR')")
    public String repartidorDashboard() {
        return "repartidor/dashboard";
    }

    @GetMapping("/cliente/dashboard")
    @PreAuthorize("hasRole('CLIENTE')")
    public String clienteDashboard() {
        return "cliente/dashboard";
    }
} 