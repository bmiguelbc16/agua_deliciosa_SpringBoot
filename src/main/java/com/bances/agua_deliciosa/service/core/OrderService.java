package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bances.agua_deliciosa.dto.client.OrderDTO;
import com.bances.agua_deliciosa.model.Order;
import com.bances.agua_deliciosa.repository.OrderRepository;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final SecurityService securityService;
    
    public List<Order> getCurrentClientOrders() {
        var currentUser = securityService.getCurrentUser();
        return orderRepository.findByClientId(currentUser.getUserableId());
    }
    
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        var currentUser = securityService.getCurrentUser();
        Order order = new Order();
        order.setClientId(currentUser.getUserableId());
        order.setQuantity(orderDTO.getQuantity());
        order.setDeliveryDate(orderDTO.getDeliveryDate());
        order.setAddress(orderDTO.getAddress());
        order.setNotes(orderDTO.getNotes());
        return orderRepository.save(order);
    }
} 