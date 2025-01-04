package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.dto.client.OrderDTO;
import com.bances.agua_deliciosa.model.Order;
import com.bances.agua_deliciosa.model.Client;
import com.bances.agua_deliciosa.model.User;
import com.bances.agua_deliciosa.repository.OrderRepository;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.data.domain.Example;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final SecurityService securityService;

    private Client getCurrentClient() {
        User user = securityService.getCurrentUser();
        return clientRepository.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Current user is not a client"));
    }

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        Client client = getCurrentClient();
        Order order = new Order();
        order.setClient(client);
        // Aquí agregarías la lógica para establecer los productos y cantidades
        return orderRepository.save(order);
    }

    public List<Order> getClientOrders() {
        Client client = getCurrentClient();
        Order probe = new Order();
        probe.setClient(client);
        return orderRepository.findAll(Example.of(probe));
    }

    public Order getOrder(Long id) {
        Client client = getCurrentClient();
        Order probe = new Order();
        probe.setId(id);
        probe.setClient(client);
        return orderRepository.findOne(Example.of(probe))
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = getOrder(id);
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}
