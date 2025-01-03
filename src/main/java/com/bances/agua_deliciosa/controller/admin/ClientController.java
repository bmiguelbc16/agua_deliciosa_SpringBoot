package com.bances.agua_deliciosa.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bances.agua_deliciosa.dto.admin.ClientDTO;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.service.core.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/clients")
@RequiredArgsConstructor
public class ClientController {
    
    private final ClientService clientService;
    
    @GetMapping
    public ResponseEntity<Page<Client>> index(
        @RequestParam(required = false) String search,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(clientService.getClientsPage(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Client> show(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }
    
    @PostMapping
    public ResponseEntity<Client> store(@Valid @RequestBody ClientDTO dto) {
        return ResponseEntity.ok(clientService.createClient(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @Valid @RequestBody ClientDTO dto) {
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
