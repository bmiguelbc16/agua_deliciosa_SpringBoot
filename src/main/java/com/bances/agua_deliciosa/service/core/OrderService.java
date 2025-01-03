// package com.bances.agua_deliciosa.service.core;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import com.bances.agua_deliciosa.dto.client.OrderDTO;
// import com.bances.agua_deliciosa.model.Order;
// import com.bances.agua_deliciosa.model.Client;
// import com.bances.agua_deliciosa.repository.OrderRepository;
// import com.bances.agua_deliciosa.repository.ClientRepository;  // Importar el repositorio de Client
// import com.bances.agua_deliciosa.service.auth.SecurityService;
// import lombok.RequiredArgsConstructor;

// import java.util.List;

// @Service
// @Transactional(readOnly = true)
// @RequiredArgsConstructor
// public class OrderService {

//     private final OrderRepository orderRepository;
//     private final SecurityService securityService;
//     private final ClientRepository clientRepository;  // Inyectar el repositorio de Client

//     // Método para obtener las órdenes del cliente actual
//     public List<Order> getCurrentClientOrders() {
//         var currentUser = securityService.getCurrentUser();

//         // Acceder al Client a través de userable_type y userable_id
//         if ("Client".equals(currentUser.getUserableType())) {
//             Long clientId = currentUser.getUserableId();
//             Client currentClient = findClientById(clientId);
//             return orderRepository.findByClientId(currentClient.getId());
//         }
//         return List.of(); // Retorna una lista vacía si no es un cliente
//     }

//     // Método para encontrar el Client a partir de userable_id
//     private Client findClientById(Long clientId) {
//         // Aquí buscar al Client usando el repositorio
//         return clientRepository.findById(clientId)
//                 .orElseThrow(() -> new RuntimeException("Client not found"));
//     }

//     // Método para crear una nueva orden
//     @Transactional
//     public Order createOrder(OrderDTO orderDTO) {
//         var currentUser = securityService.getCurrentUser();

//         // Acceder al Client a través de userable_type y userable_id
//         if ("Client".equals(currentUser.getUserableType())) {
//             Long clientId = currentUser.getUserableId();
//             Client currentClient = findClientById(clientId);

//             Order order = new Order();
//             order.setClient(currentClient); // Establecer el objeto Client completo

//             // Asignar los valores del DTO a la nueva orden
//             order.setQuantity(orderDTO.getQuantity());
//             order.setDeliveryDate(orderDTO.getDeliveryDate());
//             order.setAddress(orderDTO.getAddress());
//             order.setNotes(orderDTO.getNotes());

//             // Guardar la nueva orden
//             return orderRepository.save(order);
//         }

//         throw new RuntimeException("User is not a client");
//     }
// }
