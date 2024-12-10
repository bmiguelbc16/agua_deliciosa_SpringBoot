package com.bances.agua_deliciosa.controller.client;

import org.springframework.stereotype.Controller;
import com.bances.agua_deliciosa.controller.BaseController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/client")  // Asegurarnos de que sea "client" y no "cliente"
@PreAuthorize("hasRole('CLIENTE')")  // Este sí debe ser 'CLIENTE' porque es el nombre del rol en la BD
public class ClientDashboardController extends BaseController {
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        addCommonAttributes(model, request);
        return "client/dashboard";
    }
}
