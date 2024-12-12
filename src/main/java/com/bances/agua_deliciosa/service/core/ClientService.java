package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.dto.admin.ClientDTO;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.service.auth.CoreUserService;
import com.bances.agua_deliciosa.service.core.base.BaseService;
import com.bances.agua_deliciosa.model.User;
import org.modelmapper.ModelMapper;

@Service
@Transactional(readOnly = true)
public class ClientService extends BaseService<Client, ClientDTO> {
    
    private final CoreUserService userService;
    
    public ClientService(
        ClientRepository repository, 
        CoreUserService userService,
        ModelMapper mapper
    ) {
        super(repository, mapper);
        this.userService = userService;
    }
    
    @Override
    protected String getEntityName() {
        return "Cliente";
    }
    
    @Override
    protected Client createFromDTO(ClientDTO dto) {
        Client client = new Client();
        User user = createUserFromDTO(dto);
        client.setUser(user);
        return client;
    }
    
    private User createUserFromDTO(ClientDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setUserableType("Client");
        
        return userService.createUser(user, dto.getPassword(), "CLIENTE");
    }
    
    @Override
    protected Client updateFromDTO(Client client, ClientDTO dto) {
        User user = client.getUser();
        updateUserFromDTO(user, dto);
        return client;
    }
    
    private void updateUserFromDTO(User user, ClientDTO dto) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            userService.updatePassword(user.getId(), dto.getPassword());
        }
    }
} 