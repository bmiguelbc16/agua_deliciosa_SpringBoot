package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.exception.ResourceNotFoundException;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client save(Client client, User user) {
        if (clientRepository.existsByDocumentNumber(client.getDocumentNumber())) {
            throw new RuntimeException("Document number already exists");
        }

        client.setActive(true);
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());

        user.setUserableType("Client");
        user.setUserableId(client.getId());
        user.setRoleId(roleService.findClientRole().getId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        user = userService.save(user);
        client.setUser(user);

        return clientRepository.save(client);
    }

    @Transactional
    public Client update(Long id, Client updatedClient) {
        Client client = findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (!client.getDocumentNumber().equals(updatedClient.getDocumentNumber()) &&
            clientRepository.existsByDocumentNumberAndIdNot(updatedClient.getDocumentNumber(), id)) {
            throw new RuntimeException("Document number already exists");
        }

        client.setUser(updatedClient.getUser());
        client.setUpdatedAt(LocalDateTime.now());

        return clientRepository.save(client);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Client> client = findById(id);
        if (client.isPresent() && client.get().getUser() != null) {
            userService.deleteById(client.get().getUser().getId());
        }
        clientRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByDocumentNumber(String documentNumber) {
        return clientRepository.existsByDocumentNumber(documentNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByDocumentNumberAndIdNot(String documentNumber, Long id) {
        return clientRepository.existsByDocumentNumberAndIdNot(documentNumber, id);
    }
}
