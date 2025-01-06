package com.bances.agua_deliciosa.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.Gender;
import com.bances.agua_deliciosa.dto.admin.ClientDTO;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.UserRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<Client> getClientsPage(Pageable pageable) {
        return clientRepository.findClientsWithSearch(null, pageable);
    }

    @Transactional
    public Client createClient(ClientDTO clientDTO) {
        Client client = new Client();
        User user = new User();
        
        // Mapear datos del DTO al usuario
        user.setName(clientDTO.getName());
        user.setLastName(clientDTO.getLastName());
        user.setEmail(clientDTO.getEmail());
        user.setPhoneNumber(clientDTO.getPhoneNumber());
        user.setDocumentNumber(clientDTO.getDocumentNumber());
        user.setBirthDate(clientDTO.getBirthDate());
        user.setGender(Gender.valueOf(clientDTO.getGender()));
        user.setActive(clientDTO.getActive());
        user.setUserableType("Client");
        
        // Guardar el usuario
        user = userRepository.save(user);
        
        // Asociar el usuario al cliente
        client.setUser(user);
        
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        
        User user = client.getUser();
        
        // Actualizar datos del usuario
        user.setName(clientDTO.getName());
        user.setLastName(clientDTO.getLastName());
        user.setEmail(clientDTO.getEmail());
        user.setPhoneNumber(clientDTO.getPhoneNumber());
        user.setDocumentNumber(clientDTO.getDocumentNumber());
        user.setBirthDate(clientDTO.getBirthDate());
        user.setGender(Gender.valueOf(clientDTO.getGender()));
        user.setActive(clientDTO.getActive());
        
        // Guardar los cambios
        userRepository.save(user);
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        
        // Eliminar el usuario asociado
        if (client.getUser() != null) {
            userRepository.delete(client.getUser());
        }
        
        clientRepository.delete(client);
    }

    @Transactional(readOnly = true)
    public Client findByUserId(Long userId) {
        return clientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Client not found for user: " + userId));
    }
}
