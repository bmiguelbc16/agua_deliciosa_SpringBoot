package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import com.bances.agua_deliciosa.dto.DashboardStats;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.OrderRepository;
import com.bances.agua_deliciosa.repository.ProductRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;
    
    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        stats.setClientCount(clientRepository.count());
        stats.setOrderCount(orderRepository.count());
        stats.setProductCount(productRepository.count());
        stats.setEmployeeCount(employeeRepository.count());
        return stats;
    }
} 