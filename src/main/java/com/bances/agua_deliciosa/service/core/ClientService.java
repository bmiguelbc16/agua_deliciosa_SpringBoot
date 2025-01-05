package com.bances.agua_deliciosa.service.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.dto.admin.ClientDTO;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String CLIENT_TYPE = "Client";

    @Transactional(readOnly = true)
    public Page<Client> getClientsPage(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return clientRepository.findByUserUserableTypeAndSearchTerm(search.trim(), pageable);
        }
        return clientRepository.findByUserUserableType(pageable);
    }

    @Transactional
    public Client createClient(ClientDTO dto) {
        // Validar email único
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Validar documento único
        if (userRepository.existsByDocumentNumber(dto.getDocumentNumber())) {
            throw new RuntimeException("Document number already exists");
        }

        Role role = roleRepository.findByName("Client")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Client client = new Client();
        clientRepository.save(client);

        User user = new User();
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthDate(dto.getBirthDate());
        user.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(role);
        user.setUserableId(client.getId());
        user.setUserableType(CLIENT_TYPE);
        userRepository.save(user);

        return client;
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        User user = userRepository.findByUserableIdAndUserableType(client.getId(), CLIENT_TYPE)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setDocumentNumber(user.getDocumentNumber());
        dto.setBirthDate(user.getBirthDate());
        dto.setGender(user.getGender().name());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRoleId(user.getRole().getId());
        return dto;
    }

    @Transactional
    public Client updateClient(Long id, ClientDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        User user = userRepository.findByUserableIdAndUserableType(client.getId(), CLIENT_TYPE)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validar email único
        if (!user.getEmail().equals(dto.getEmail()) &&
            userRepository.existsByEmailAndIdNot(dto.getEmail(), user.getId())) {
            throw new RuntimeException("Email already exists");
        }

        // Validar documento único
        if (!user.getDocumentNumber().equals(dto.getDocumentNumber()) &&
            userRepository.existsByDocumentNumberAndIdNot(dto.getDocumentNumber(), user.getId())) {
            throw new RuntimeException("Document number already exists");
        }

        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userRepository.save(user);

        return client;
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        User user = userRepository.findByUserableIdAndUserableType(client.getId(), CLIENT_TYPE)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        userRepository.delete(user);
        clientRepository.delete(client);
    }
}
