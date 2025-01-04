package com.bances.agua_deliciosa.service.core;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.dto.DashboardDTO;
import com.bances.agua_deliciosa.dto.DashboardDTO.*;
import com.bances.agua_deliciosa.dto.DashboardStats;
import com.bances.agua_deliciosa.model.OrderStatus;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.OrderRepository;
import com.bances.agua_deliciosa.repository.ProductRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;
    
    public DashboardStats getStats() {
        try {
            log.info("Fetching dashboard statistics");
            
            DashboardStats stats = new DashboardStats();
            stats.setTotalCustomers(clientRepository.count());
            stats.setTotalOrders(orderRepository.count());
            stats.setTotalProducts(productRepository.count());
            stats.setTotalEmployees(employeeRepository.count());
            
            log.info("Successfully fetched dashboard statistics");
            return stats;
        } catch (Exception e) {
            log.error("Error fetching dashboard statistics", e);
            throw new RuntimeException("Error fetching dashboard statistics", e);
        }
    }

    public DashboardDTO getDashboardData() {
        try {
            log.info("Fetching dashboard data");
            
            DashboardDTO dashboard = new DashboardDTO();
            
            // Obtener estadísticas básicas
            dashboard.setStats(getStats());
            
            // Obtener pedidos recientes
            dashboard.setRecentOrders(orderRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(order -> {
                    OrderDTO dto = new OrderDTO();
                    dto.setCustomerName(order.getClient().getUser().getName() + " " + 
                                      order.getClient().getUser().getLastName());
                    dto.setTotal(order.getTotal().doubleValue());
                    String status = order.getStatus();
                    dto.setStatus(status);
                    dto.setStatusClass(getStatusClassFromString(status));
                    return dto;
                })
                .toList());
            
            // Obtener productos más vendidos
            List<Object[]> topProductsData = orderRepository.findTop5Products();
            dashboard.setTopProducts(topProductsData.stream()
                .map(data -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setName((String) data[1]);
                    dto.setPrice(((Number) data[2]).doubleValue());
                    dto.setSoldCount(((Number) data[3]).intValue());
                    return dto;
                })
                .toList());
            
            // Obtener mejores clientes
            List<Object[]> topCustomersData = orderRepository.findTop5Customers();
            dashboard.setTopCustomers(topCustomersData.stream()
                .map(data -> {
                    CustomerDTO dto = new CustomerDTO();
                    dto.setName((String) data[1]);
                    dto.setOrderCount(((Number) data[2]).intValue());
                    dto.setTotalSpent(((Number) data[3]).doubleValue());
                    return dto;
                })
                .toList());
            
            log.info("Successfully fetched dashboard data");
            return dashboard;
        } catch (Exception e) {
            log.error("Error fetching dashboard data", e);
            throw new RuntimeException("Error fetching dashboard data", e);
        }
    }
    
    private String getStatusClassFromString(String status) {
        try {
            return getStatusClass(OrderStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid order status: {}", status);
            return "bg-secondary";
        }
    }
    
    private String getStatusClass(OrderStatus status) {
        return switch (status) {
            case PENDING -> "bg-warning";
            case CONFIRMED -> "bg-info";
            case IN_PROCESS -> "bg-primary";
            case DELIVERED -> "bg-success";
            case CANCELLED -> "bg-danger";
            default -> "bg-secondary";
        };
    }
}