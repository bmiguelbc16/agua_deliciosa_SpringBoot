package com.bances.agua_deliciosa.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.util.Routes;
import com.bances.agua_deliciosa.dto.admin.ClientDTO;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.ClientService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(Routes.Admin.CLIENTS)
public class ClientController extends AdminController {
    
    private final ClientService clientService;
    
    public ClientController(SecurityService securityService, ClientService clientService) {
        super(securityService);
        this.clientService = clientService;
    }

    @GetMapping
    public String index(
        @RequestParam(required = false) String search,
        @PageableDefault(size = 10) Pageable pageable,
        Model model
    ) {
        setupCommonAttributes(model, "clients");
        model.addAttribute("title", "Gestión de Clientes");
        model.addAttribute("clients", clientService.getClientsPage(pageable));
        return view("clients/index");
    }

    @GetMapping("/create")
    public String create(Model model) {
        setupCommonAttributes(model, "clients");
        model.addAttribute("title", "Nuevo Cliente");
        model.addAttribute("client", new ClientDTO());
        return view("clients/create");
    }

    @PostMapping("/create")
    public String store(
        @Valid @ModelAttribute("client") ClientDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            clientService.createClient(dto);
            addSuccessMessage(redirectAttributes, "Cliente creado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al crear el cliente: " + e.getMessage());
            return redirect("create");
        }
        return redirect("");
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        setupCommonAttributes(model, "clients");
        model.addAttribute("title", "Editar Cliente");
        model.addAttribute("client", clientService.getClientById(id));
        return view("clients/edit");
    }

    @PostMapping("/{id}/update")
    public String updateClient(
        @PathVariable Long id,
        @Valid @ModelAttribute("client") ClientDTO dto,
        RedirectAttributes redirectAttributes
    ) {
        try {
            clientService.updateClient(id, dto);
            addSuccessMessage(redirectAttributes, "Cliente actualizado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al actualizar el cliente: " + e.getMessage());
            return redirect(id + "/edit");
        }
        return redirect("");
    }

    @PostMapping("/{id}/delete")
    public String delete(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes
    ) {
        try {
            clientService.deleteClient(id);
            addSuccessMessage(redirectAttributes, "Cliente eliminado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al eliminar el cliente: " + e.getMessage());
        }
        return redirect("");
    }
}
