package com.bances.agua_deliciosa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import com.bances.agua_deliciosa.dto.ClientDTO;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import com.bances.agua_deliciosa.model.Role;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Client findById(Long id) {
        return clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public Page<Client> findAll(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return clientRepository.findByUserNameOrLastName(search, pageable);
        }
        return clientRepository.findAll(pageable);
    }

    @Transactional
    public Client create(ClientDTO dto) {
        // Primero crear el cliente
        Client client = new Client();
        clientRepository.save(client);
        
        // Luego crear el usuario asociado
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserableType("Client");
        user.setUserableId(client.getId());
        
        // Asignar rol de cliente
        Role clientRole = roleRepository.findByName("ROLE_CLIENTE")
            .orElseThrow(() -> new RuntimeException("Rol cliente no encontrado"));
        user.setRoles(Collections.singleton(clientRole));
        
        userRepository.save(user);
        
        return client;
    }

    @Transactional
    public Client update(Long id, ClientDTO dto) {
        Client client = findById(id);
        
        // Actualizar usuario
        User user = client.getUser();
        user.setEmail(dto.getEmail());
        user.setName(dto.getNombres());
        user.setLastName(dto.getApellidos());
        user.setDocumentNumber(dto.getDni());
        user.setPhoneNumber(dto.getTelefono());
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        userRepository.save(user);
        return client;
    }

    @Transactional
    public void delete(Long id) {
        Client client = findById(id);
        clientRepository.delete(client);
        userRepository.delete(client.getUser());
    }
} 