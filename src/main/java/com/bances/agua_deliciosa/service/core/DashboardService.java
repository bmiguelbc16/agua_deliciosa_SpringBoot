package com.bances.agua_deliciosa.service.core;

import com.bances.agua_deliciosa.model.Order;
import com.bances.agua_deliciosa.model.OrderStatus;
import com.bances.agua_deliciosa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardData() {
        List<Order> orders = orderRepository.findAll();
        Map<String, Object> dashboardData = new HashMap<>();

        // Contadores de órdenes
        long totalOrders = orders.size();
        long pendingOrders = orders.stream()
            .filter(order -> order.getStatus().equals(OrderStatus.PENDING.name()))
            .count();
        long completedOrders = orders.stream()
            .filter(order -> order.getStatus().equals(OrderStatus.COMPLETED.name()))
            .count();
        long cancelledOrders = orders.stream()
            .filter(order -> order.getStatus().equals(OrderStatus.CANCELLED.name()))
            .count();

        // Ingresos totales
        BigDecimal totalIncome = orders.stream()
            .filter(order -> order.getStatus().equals(OrderStatus.COMPLETED.name()))
            .map(Order::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Agregar datos al mapa
        dashboardData.put("totalOrders", totalOrders);
        dashboardData.put("pendingOrders", pendingOrders);
        dashboardData.put("completedOrders", completedOrders);
        dashboardData.put("cancelledOrders", cancelledOrders);
        dashboardData.put("totalIncome", totalIncome);

        return dashboardData;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalClients", userService.countClients());
        stats.put("totalEmployees", userService.countEmployees());
        stats.put("totalOrders", orderService.count());
        stats.put("recentOrders", orderService.findRecent(5));
        stats.put("topCustomers", userService.findTopCustomers(5));
        stats.put("totalProducts", productService.count());
        stats.put("topProducts", productService.findTopSelling(5));
        return stats;
    }
}