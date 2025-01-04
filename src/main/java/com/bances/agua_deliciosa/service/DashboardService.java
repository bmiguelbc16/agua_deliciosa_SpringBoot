package com.bances.agua_deliciosa.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bances.agua_deliciosa.dto.DashboardDTO;
import com.bances.agua_deliciosa.dto.DashboardDTO.*;
import com.bances.agua_deliciosa.repository.ClienteRepository;
import com.bances.agua_deliciosa.repository.EmpleadoRepository;
import com.bances.agua_deliciosa.repository.PedidoRepository;
import com.bances.agua_deliciosa.repository.ProductoRepository;

@Service
public class DashboardService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;

    public DashboardDTO getDashboardData() {
        DashboardDTO dashboard = new DashboardDTO();
        
        try {
            // Estadísticas generales
            StatsDTO stats = new StatsDTO();
            stats.setTotalCustomers(clienteRepository.count());
            stats.setTotalOrders(pedidoRepository.count());
            stats.setTotalProducts(productoRepository.count());
            stats.setTotalEmployees(empleadoRepository.count());
            dashboard.setStats(stats);
            
            // Ingresos mensuales (datos de ejemplo si el método no está implementado)
            try {
                dashboard.setMonthlyRevenue(pedidoRepository.getMonthlyRevenue());
            } catch (Exception e) {
                List<MonthlyRevenueDTO> demoRevenue = new ArrayList<>();
                // Agregar datos de ejemplo...
                dashboard.setMonthlyRevenue(demoRevenue);
            }
            
            // Pedidos recientes
            try {
                dashboard.setRecentOrders(pedidoRepository.getRecentOrders());
            } catch (Exception e) {
                List<RecentOrderDTO> demoOrders = new ArrayList<>();
                // Agregar datos de ejemplo...
                dashboard.setRecentOrders(demoOrders);
            }
            
            // Productos más vendidos
            try {
                dashboard.setTopProducts(pedidoRepository.getTopProducts());
            } catch (Exception e) {
                List<TopProductDTO> demoProducts = new ArrayList<>();
                // Agregar datos de ejemplo...
                dashboard.setTopProducts(demoProducts);
            }
            
            // Mejores clientes
            try {
                dashboard.setTopCustomers(pedidoRepository.getTopCustomers());
            } catch (Exception e) {
                List<TopCustomerDTO> demoCustomers = new ArrayList<>();
                // Agregar datos de ejemplo...
                dashboard.setTopCustomers(demoCustomers);
            }
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            
            // Create empty stats if there's an error
            StatsDTO stats = new StatsDTO();
            stats.setTotalCustomers(0);
            stats.setTotalOrders(0);
            stats.setTotalProducts(0);
            stats.setTotalEmployees(0);
            dashboard.setStats(stats);
        }
        
        return dashboard;
    }
}
