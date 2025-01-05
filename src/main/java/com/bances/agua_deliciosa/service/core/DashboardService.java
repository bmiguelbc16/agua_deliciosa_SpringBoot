package com.bances.agua_deliciosa.service.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.dto.admin.DashboardDTO;
import com.bances.agua_deliciosa.dto.admin.summary.OrderSummaryDTO;
import com.bances.agua_deliciosa.dto.admin.summary.ProductSummaryDTO;
import com.bances.agua_deliciosa.dto.admin.summary.ClientSummaryDTO;
import com.bances.agua_deliciosa.model.Order;
import com.bances.agua_deliciosa.model.OrderStatus;
import com.bances.agua_deliciosa.repository.OrderRepository;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public DashboardDTO getDashboardData() {
        long totalClients = clientRepository.count();
        long totalEmployees = employeeRepository.count();
        long totalOrders = orderRepository.count();
        long totalProducts = productRepository.count();
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        long inDeliveryOrders = orderRepository.countByStatus(OrderStatus.IN_DELIVERY);
        
        List<Order> recentOrders = orderRepository.findTop5ByOrderByCreatedAtDesc();
        
        return DashboardDTO.builder()
            .totalClients(totalClients)
            .totalEmployees(totalEmployees)
            .totalOrders(totalOrders)
            .totalProducts(totalProducts)
            .pendingOrders(pendingOrders)
            .inDeliveryOrders(inDeliveryOrders)
            .recentOrders(recentOrders.stream()
                .map(this::toOrderSummaryDTO)
                .toList())
            .topProducts(orderRepository.findTop5Products().stream()
                .map(this::toProductSummaryDTO)
                .toList())
            .topCustomers(orderRepository.findTop5Customers().stream()
                .map(this::toClientSummaryDTO)
                .toList())
            .build();
    }

    private OrderSummaryDTO toOrderSummaryDTO(Order order) {
        return OrderSummaryDTO.builder()
            .id(order.getId())
            .orderNumber(order.getOrderNumber())
            .clientName(order.getClient().getUser().getName() + " " + 
                      order.getClient().getUser().getLastName())
            .totalAmount(order.getTotal())
            .status(order.getStatus())
            .statusClass(getStatusClass(order.getStatus()))
            .createdAt(order.getCreatedAt())
            .build();
    }

    private ProductSummaryDTO toProductSummaryDTO(Object[] data) {
        return ProductSummaryDTO.builder()
            .name((String) data[1])
            .price(((Number) data[2]).doubleValue())
            .soldCount(((Number) data[3]).intValue())
            .build();
    }

    private ClientSummaryDTO toClientSummaryDTO(Object[] data) {
        return ClientSummaryDTO.builder()
            .name((String) data[1])
            .orderCount(((Number) data[2]).intValue())
            .totalSpent(((Number) data[3]).doubleValue())
            .build();
    }
    
    private String getStatusClass(OrderStatus status) {
        return switch (status) {
            case PENDING -> "bg-warning";
            case IN_PROCESS -> "bg-info";
            case IN_DELIVERY -> "bg-primary";
            case DELIVERED -> "bg-success";
            case CANCELLED -> "bg-danger";
            default -> "bg-secondary";
        };
    }
}