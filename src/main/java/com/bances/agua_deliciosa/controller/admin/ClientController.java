package com.bances.agua_deliciosa.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import com.bances.agua_deliciosa.dto.ClientDTO;
import com.bances.agua_deliciosa.service.ClientService;
import com.bances.agua_deliciosa.model.Client;

@Controller
@RequestMapping("/admin/clients")
@Slf4j
public class ClientController extends AdminController {
    
    @Autowired
    private ClientService clientService;
    
    @GetMapping
    public String index(Model model, 
                        @RequestParam(required = false) String search,
                        @RequestParam(defaultValue = "0") int page) {
        Page<Client> clients = clientService.findAll(search, PageRequest.of(page, 10));
        model.addAttribute("clients", clients);
        return "admin/clients/index";
    }
    
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("client", new ClientDTO());
        model.addAttribute("genderOptions", Client.OPTIONS_GENDER);
        return "admin/clients/create";
    }
    
    @PostMapping
    public String store(@Valid ClientDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/clients/create";
        }
        clientService.create(dto);
        return "redirect:/admin/clients";
    }
} 