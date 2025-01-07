package com.bances.agua_deliciosa.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bances.agua_deliciosa.dto.admin.ClientDTO;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.service.core.ClientService;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

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
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<Client> clientPage = clientService.getClients(page, size, null);
        model.addAttribute("clients", clientPage);
        return "admin/clients/index";
    }

    @GetMapping("/api")
    @ResponseBody
    public Map<String, Object> getClients(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Client> clientPage = clientService.getClients(page, size, null);
        Map<String, Object> response = new HashMap<>();
        response.put("clients", clientPage.getContent());
        response.put("currentPage", clientPage.getNumber());
        response.put("totalItems", clientPage.getTotalElements());
        response.put("totalPages", clientPage.getTotalPages());
        return response;
    }

    @PostMapping
    @ResponseBody
    public Client create(@Valid @RequestBody ClientDTO clientDTO) {
        return clientService.createClient(clientDTO);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Client update(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        return clientService.updateClient(id, clientDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}
