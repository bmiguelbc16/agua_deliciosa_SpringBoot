package com.bances.agua_deliciosa.db.seeders;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bances.agua_deliciosa.model.*;
import com.bances.agua_deliciosa.repository.*;
import java.time.LocalDate;

@Component
public class UserSeeder {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void seed() {
        // Crear admin
        Employee admin = employeeRepository.save(new Employee());
        
        User userAdmin = new User();
        userAdmin.setDocumentNumber("00000000");
        userAdmin.setName("Admin");
        userAdmin.setLastName("System");
        userAdmin.setBirthDate(LocalDate.now());
        userAdmin.setGender("M");
        userAdmin.setEmail("admin@aguadeliciosa.com");
        userAdmin.setUserableType(Employee.class.getName());
        userAdmin.setUserableId(admin.getId());
        userAdmin.setPassword(passwordEncoder.encode("admin123456"));
        userAdmin.setActive(true);
        userAdmin.getRoles().add(roleRepository.findByName("ROLE_ADMIN")
            .orElseThrow(() -> new RuntimeException("Role not found")));
        
        userRepository.save(userAdmin);

        // Crear gestor
        Employee gestor = employeeRepository.save(new Employee());
        
        User userGestor = new User();
        userGestor.setDocumentNumber("11111111");
        userGestor.setName("Gestor");
        userGestor.setLastName("Pedidos");
        userGestor.setBirthDate(LocalDate.now());
        userGestor.setGender("M");
        userGestor.setEmail("gestor@aguadeliciosa.com");
        userGestor.setUserableType(Employee.class.getName());
        userGestor.setUserableId(gestor.getId());
        userGestor.setPassword(passwordEncoder.encode("gestorp123456"));
        userGestor.setActive(true);
        userGestor.getRoles().add(roleRepository.findByName("ROLE_GESTOR")
            .orElseThrow(() -> new RuntimeException("Role not found")));
            
        userRepository.save(userGestor);

        // Crear almacenero
        Employee almacenero = employeeRepository.save(new Employee());
        
        User userAlmacenero = new User();
        userAlmacenero.setDocumentNumber("22222222");
        userAlmacenero.setName("Almacen");
        userAlmacenero.setLastName("Stock");
        userAlmacenero.setBirthDate(LocalDate.now());
        userAlmacenero.setGender("M");
        userAlmacenero.setEmail("almacen@aguadeliciosa.com");
        userAlmacenero.setUserableType(Employee.class.getName());
        userAlmacenero.setUserableId(almacenero.getId());
        userAlmacenero.setPassword(passwordEncoder.encode("almacenero123456"));
        userAlmacenero.setActive(true);
        userAlmacenero.getRoles().add(roleRepository.findByName("ROLE_ALMACEN")
            .orElseThrow(() -> new RuntimeException("Role not found")));
            
        userRepository.save(userAlmacenero);

        // Crear vendedor
        Employee vendedor = employeeRepository.save(new Employee());
        
        User userVendedor = new User();
        userVendedor.setDocumentNumber("33333333");
        userVendedor.setName("Vendedor");
        userVendedor.setLastName("Ventas");
        userVendedor.setBirthDate(LocalDate.now());
        userVendedor.setGender("M");
        userVendedor.setEmail("vendedor@aguadeliciosa.com");
        userVendedor.setUserableType(Employee.class.getName());
        userVendedor.setUserableId(vendedor.getId());
        userVendedor.setPassword(passwordEncoder.encode("vendedor123456"));
        userVendedor.setActive(true);
        userVendedor.getRoles().add(roleRepository.findByName("ROLE_VENDEDOR")
            .orElseThrow(() -> new RuntimeException("Role not found")));
            
        userRepository.save(userVendedor);

        // Crear cliente
        Client client = clientRepository.save(new Client());
        
        User userClient = new User();
        userClient.setDocumentNumber("44444444");
        userClient.setName("Cliente");
        userClient.setLastName("Test");
        userClient.setBirthDate(LocalDate.now());
        userClient.setGender("M");
        userClient.setEmail("cliente@aguadeliciosa.com");
        userClient.setUserableType(Client.class.getName());
        userClient.setUserableId(client.getId());
        userClient.setPassword(passwordEncoder.encode("cliente123456"));
        userClient.setActive(true);
        userClient.getRoles().add(roleRepository.findByName("ROLE_CLIENTE")
            .orElseThrow(() -> new RuntimeException("Role not found")));
            
        userRepository.save(userClient);
    }
} 