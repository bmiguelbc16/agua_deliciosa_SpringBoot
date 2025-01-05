package com.bances.agua_deliciosa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.model.Order;
import com.bances.agua_deliciosa.model.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Encuentra los últimos 5 pedidos ordenados por fecha de creación descendente
     */
    @Query(value = "SELECT o.* FROM orders o ORDER BY o.created_at DESC LIMIT 5", nativeQuery = true)
    List<Order> findTop5ByOrderByCreatedAtDesc();
    
    /**
     * Encuentra los pedidos de un cliente específico
     */
    List<Order> findByClientId(Long clientId);
    
    /**
     * Cuenta el número de pedidos por estado
     */
    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countByStatus();
    
    /**
     * Cuenta el número de pedidos por estado
     */
    long countByStatus(OrderStatus status);
    
    /**
     * Encuentra los pedidos entre dos fechas
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(String startDate, String endDate);
    
    /**
     * Calcula el total de ventas por mes
     */
    @Query(value = """
        SELECT 
            YEAR(created_at) as year,
            MONTH(created_at) as month,
            SUM(total_amount) as total
        FROM orders 
        WHERE created_at >= DATE_SUB(NOW(), INTERVAL 12 MONTH)
        GROUP BY YEAR(created_at), MONTH(created_at)
        ORDER BY year DESC, month DESC
        LIMIT 12
    """, nativeQuery = true)
    List<Object[]> getMonthlyRevenue();
    
    /**
     * Encuentra los productos más vendidos
     */
    @Query(value = """
        SELECT p.id, p.name, p.price, COUNT(*) as quantity 
        FROM orders o 
        JOIN order_details od ON o.id = od.order_id 
        JOIN products p ON od.product_id = p.id 
        GROUP BY p.id, p.name, p.price 
        ORDER BY quantity DESC 
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findTop5Products();
    
    /**
     * Encuentra los mejores clientes
     */
    @Query(value = """
        SELECT c.id, CONCAT(u.name, ' ', u.last_name) as full_name, 
        COUNT(o.id) as total_orders, SUM(o.total_amount) as total_spent 
        FROM orders o 
        JOIN clients c ON o.client_id = c.id 
        JOIN users u ON u.userable_id = c.id AND u.userable_type = 'Client'
        GROUP BY c.id, u.name, u.last_name 
        ORDER BY total_spent DESC 
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findTop5Customers();
}