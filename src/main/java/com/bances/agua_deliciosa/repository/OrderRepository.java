package com.bances.agua_deliciosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.bances.agua_deliciosa.model.Order;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findTop5ByOrderByRegistrationDateDesc();
    
    @Query("SELECT COUNT(o) FROM Order o WHERE DATE(o.registrationDate) = CURRENT_DATE")
    Long countTodayOrders();
    
    @Query("SELECT COUNT(o) FROM Order o WHERE YEAR(o.registrationDate) = YEAR(CURRENT_DATE) AND MONTH(o.registrationDate) = MONTH(CURRENT_DATE)")
    Long countMonthOrders();
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    BigDecimal calculateTotalSales();
    
    @Query("SELECT o FROM Order o WHERE o.client.user.name LIKE %:search% OR o.client.user.lastName LIKE %:search%")
    Page<Order> findByClientNameContaining(String search, Pageable pageable);
    
    List<Order> findByClientId(Long clientId);
    
    @Query("SELECT o FROM Order o WHERE o.client.id = :clientId ORDER BY o.registrationDate DESC")
    Page<Order> findByClientIdOrderByDateDesc(Long clientId, Pageable pageable);
    
    // Ventas por mes (últimos 12 meses)
    @Query("SELECT FUNCTION('MONTH', o.registrationDate) as month, SUM(o.totalAmount) as total " +
            "FROM Order o " +
            "WHERE o.registrationDate >= :startDate " +
            "GROUP BY FUNCTION('MONTH', o.registrationDate) " +
            "ORDER BY month")
    List<Object[]> getMonthlySales(LocalDateTime startDate);
    
    // Ventas por día (últimos 7 días)
    @Query("SELECT DATE(o.registrationDate) as date, SUM(o.totalAmount) as total " +
            "FROM Order o " +
            "WHERE o.registrationDate >= :startDate " +
            "GROUP BY DATE(o.registrationDate) " +
            "ORDER BY date")
    List<Object[]> getDailySales(LocalDateTime startDate);
    
    // Productos más vendidos
    @Query("SELECT od.product.name, SUM(od.quantity) as quantity " +
            "FROM OrderDetail od " +
            "GROUP BY od.product.id, od.product.name " +
            "ORDER BY quantity DESC")
    List<Object[]> getTopSellingProducts(Pageable pageable);
    
    // Crecimiento de ventas (comparación con mes anterior)
    @Query("SELECT " +
            "(SELECT COALESCE(SUM(o1.totalAmount), 0) FROM Order o1 WHERE YEAR(o1.registrationDate) = YEAR(CURRENT_DATE) AND MONTH(o1.registrationDate) = MONTH(CURRENT_DATE)) as currentMonth, " +
            "(SELECT COALESCE(SUM(o2.totalAmount), 0) FROM Order o2 WHERE YEAR(o2.registrationDate) = YEAR(CURRENT_DATE) AND MONTH(o2.registrationDate) = MONTH(CURRENT_DATE) - 1) as previousMonth")
    Object[] getSalesGrowth();
    
    // Promedio de ventas por día de la semana
    @Query("SELECT FUNCTION('DAYOFWEEK', o.registrationDate) as dayOfWeek, AVG(o.totalAmount) as average " +
            "FROM Order o " +
            "GROUP BY FUNCTION('DAYOFWEEK', o.registrationDate) " +
            "ORDER BY dayOfWeek")
    List<Object[]> getAverageSalesByDayOfWeek();
    
    // Distribución de ventas por zona/distrito
    @Query("SELECT o.deliveryAddress, COUNT(o) as count, SUM(o.totalAmount) as total " +
            "FROM Order o " +
            "GROUP BY o.deliveryAddress " +
            "ORDER BY count DESC")
    List<Object[]> getSalesByLocation();
} 