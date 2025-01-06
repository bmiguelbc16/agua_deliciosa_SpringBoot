package com.bances.agua_deliciosa.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bances.agua_deliciosa.dto.admin.ClientDTO;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.Employee;
import com.bances.agua_deliciosa.service.core.ClientService;
import com.bances.agua_deliciosa.service.core.RoleService;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import jakarta.validation.Valid;

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
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model
    ) {

        setupCommonAttributes(model, "clients");

        Pageable paging = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Client> clientPage = clientService.getClientsPage(paging);

        model.addAttribute("clients", clientPage);
        model.addAttribute("currentPage", clientPage.getNumber());
        model.addAttribute("totalItems", clientPage.getTotalElements());
        model.addAttribute("totalPages", clientPage.getTotalPages());

        return "admin/clients/index";

    }

    @GetMapping("/create")
    public String create(Model model) {
        setupCommonAttributes(model, "clients");
        model.addAttribute("title", "Crear Cliente");
        model.addAttribute("client", new ClientDTO());
        // model.addAttribute("roles", roleService.getAllRoles());
        return view("clients/create");
    }

    @PostMapping("/create")
    public String store(
            @Valid @ModelAttribute("client") ClientDTO clientDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            setupCommonAttributes(model, "clients");
            model.addAttribute("title", "Crear Cliente");
            // model.addAttribute("roles", roleService.getAllRoles());
            return view("clients/create");
        }

        try {
            clientService.createClient(clientDTO);
            addSuccessMessage(redirectAttributes, "Cliente creado exitosamente");
            return redirect("");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al crear el cliente: " + e.getMessage());
            return redirect("create");
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        setupCommonAttributes(model, "clients");
        model.addAttribute("title", "Editar Cliente");
        model.addAttribute("client", clientService.findByUserId(id));
        // model.addAttribute("roles", roleService.getAllRoles());
        return view("clients/edit");
    }

    @PostMapping("/{id}/update")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("client") ClientDTO clientDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            setupCommonAttributes(model, "clients");
            model.addAttribute("title", "Editar Cliente");
            // model.addAttribute("roles", roleService.getAllRoles());
            return view("clients/edit");
        }

        try {
            clientService.updateClient(id, clientDTO);
            addSuccessMessage(redirectAttributes, "Cliente actualizado exitosamente");
            return redirect("");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al actualizar el cliente: " + e.getMessage());
            return redirect(id + "/edit");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        clientService.deleteClient(id);
    }

    @Override
    protected String getViewPrefix() {
        return "admin/clients";
    }
}
