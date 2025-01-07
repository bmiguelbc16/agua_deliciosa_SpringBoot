package com.bances.agua_deliciosa.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bances.agua_deliciosa.model.OrderStatus;
import com.bances.agua_deliciosa.repository.OrderRepository;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;
import com.bances.agua_deliciosa.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("totalClients", clientRepository.count());
        dashboard.put("totalEmployees", employeeRepository.count());
        dashboard.put("totalProducts", productRepository.count());
        dashboard.put("totalOrders", orderRepository.count());
        dashboard.put("totalPendingOrders", orderRepository.countByStatus(OrderStatus.PENDING));
        dashboard.put("totalCompletedOrders", orderRepository.countByStatus(OrderStatus.COMPLETED));
        
        // Calcular total de ventas
        BigDecimal totalSales = orderRepository.calculateTotalSales();
        dashboard.put("totalSales", totalSales != null ? totalSales : BigDecimal.ZERO);
        
        return dashboard;
    }
}