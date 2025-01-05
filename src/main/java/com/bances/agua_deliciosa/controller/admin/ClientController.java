package com.bances.agua_deliciosa.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.ClientService;

@Controller
@RequestMapping("/admin/clients")
public class ClientController extends AdminController {

    private final ClientService clientService;

    public ClientController(SecurityService securityService, ClientService clientService) {
        super(securityService);
        this.clientService = clientService;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String search,
            Pageable pageable,
            Model model
    ) {
        setupCommonAttributes(model, "clients");
        model.addAttribute("title", "Gestión de Clientes");
        model.addAttribute("clients", clientService.getClientsPage(search, pageable));
        return view("clients/index");
    }
}
