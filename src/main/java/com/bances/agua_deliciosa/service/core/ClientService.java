package com.bances.agua_deliciosa.service.core;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bances.agua_deliciosa.dto.admin.ClientDTO;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.UserRepository;
import com.bances.agua_deliciosa.repository.RoleRepository;
import com.bances.agua_deliciosa.model.Role;
import com.bances.agua_deliciosa.model.Gender;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClientService {
    
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Transactional
    public Client createClient(ClientDTO dto) {
        Client client = new Client();
        clientRepository.save(client);

        User user = new User();
        updateUserFromDTO(user, dto);
        user.setPassword(passwordEncoder.encode(dto.getDocumentNumber()));
        user.setUserableType("Client");
        user.setUserableId(client.getId());

        // Asignar rol de Cliente
        Role clientRole = roleRepository.findByName("Cliente")
            .orElseThrow(() -> new RuntimeException("Rol Cliente no encontrado"));
        user.setRole(clientRole);

        userRepository.save(user);
        return client;
    }

    @Transactional
    public Client updateClient(Long id, ClientDTO dto) {
        Client client = getClientById(id);
        User user = client.getUser();

        // Validar datos únicos
        if (!user.getEmail().equals(dto.getEmail()) && 
            userRepository.existsByEmailAndIdNot(dto.getEmail(), user.getId())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (!user.getDocumentNumber().equals(dto.getDocumentNumber()) && 
            userRepository.existsByDocumentNumberAndIdNot(dto.getDocumentNumber(), user.getId())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        // Actualizar datos del usuario
        updateUserFromDTO(user, dto);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);
        return client;
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = getClientById(id);
        User user = client.getUser();
        clientRepository.delete(client);
        userRepository.delete(user);
    }

    private void updateUserFromDTO(User user, ClientDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setGender(Gender.valueOf(dto.getGender()));
        user.setBirthDate(dto.getBirthDate());
        user.setActive(true);
        
        // El rol se establece automáticamente en el DTO
        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            user.setRole(role);
        }
    }

    public Page<Client> getClientsPage(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public long getClientCount() {
        return clientRepository.count();
    }
}
