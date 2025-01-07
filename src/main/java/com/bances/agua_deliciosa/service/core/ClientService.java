package com.bances.agua_deliciosa.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client save(Client client, User user) {
        validateNewClient(client);
        
        // Encriptar contraseña y guardar usuario
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        
        // Asignar rol de cliente
        userService.assignRole(savedUser, "ROLE_CLIENT");
        
        // Guardar cliente
        Client savedClient = clientRepository.save(client);
        
        return savedClient;
    }

    @Transactional
    public Client update(Client client) {
        Client existingClient = findById(client.getId());
        
        if (!existingClient.getEmail().equals(client.getEmail())) {
            validateEmail(client.getEmail());
        }
        
        return clientRepository.save(client);
    }

    @Transactional
    public void delete(Long id) {
        Client client = findById(id);
        clientRepository.delete(id);
        
        // Si tiene usuario asociado, desactivarlo también
        if (client.getUser() != null) {
            userRepository.delete(client.getUser().getId());
        }
    }

    private void validateNewClient(Client client) {
        validateEmail(client.getEmail());
    }

    private void validateEmail(String email) {
        if (clientRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists: " + email);
        }
    }
}
