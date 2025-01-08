package com.bances.agua_deliciosa.controller.admin;

import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.service.core.ClientService;
import com.bances.agua_deliciosa.service.core.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final RoleService roleService;

    protected String view(String viewName) {
        return "admin/clients/" + viewName;
    }

    protected String redirect(String path) {
        return "redirect:" + path;
    }

    protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("successMessage", message);
    }

    protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("errorMessage", message);
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return view("index");
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("user", new User());
        return view("form");
    }

    @PostMapping
    public String store(
            @ModelAttribute Client client,
            @ModelAttribute User user,
            RedirectAttributes redirectAttributes
    ) {
        try {
            user.setRoleId(roleService.findClientRole().getId());
            clientService.save(client, user);
            addSuccessMessage(redirectAttributes, "Cliente creado exitosamente");
            return redirect("/admin/clients");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/admin/clients/create");
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("client", client);
        return view("form");
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute Client client,
            @ModelAttribute User user,
            RedirectAttributes redirectAttributes
    ) {
        try {
            client.setId(id);
            user.setRoleId(roleService.findClientRole().getId());
            clientService.save(client, user);
            addSuccessMessage(redirectAttributes, "Cliente actualizado exitosamente");
            return redirect("/admin/clients");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return redirect("/admin/clients/" + id + "/edit");
        }
    }

    @DeleteMapping("/{id}")
    public String destroy(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            clientService.deleteById(id);
            addSuccessMessage(redirectAttributes, "Cliente eliminado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
        }
        return redirect("/admin/clients");
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("client", client);
        return view("show");
    }
}
