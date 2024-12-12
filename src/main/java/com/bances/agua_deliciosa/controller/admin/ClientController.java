package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.dto.admin.ClientDTO;
import com.bances.agua_deliciosa.service.core.ClientService;
import com.bances.agua_deliciosa.controller.base.BaseCrudController;

@Controller
@RequestMapping("/admin/clients")
public class ClientController extends BaseCrudController<Client, ClientDTO> {
    
    public ClientController(ClientService clientService) {
        super(clientService);
    }
    
    @Override
    protected String getBasePath() {
        return "/admin/clients";
    }
    
    @Override
    protected String getViewPrefix() {
        return "admin/clients";
    }
    
    @Override
    protected String getEntityName() {
        return "Cliente";
    }
    
    @Override
    protected void addCommonAttributes(Model model, HttpServletRequest request) {
        super.addCommonAttributes(model, request);
        model.addAttribute("pageTitle", "Gestión de Clientes");
    }
}