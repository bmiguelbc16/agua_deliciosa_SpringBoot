package com.bances.agua_deliciosa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bances.agua_deliciosa.dto.DashboardDTO.MonthlyRevenueDTO;
import com.bances.agua_deliciosa.dto.DashboardDTO.RecentOrderDTO;
import com.bances.agua_deliciosa.dto.DashboardDTO.TopCustomerDTO;
import com.bances.agua_deliciosa.dto.DashboardDTO.TopProductDTO;
import com.bances.agua_deliciosa.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
    @Query(value = """
            SELECT new com.bances.agua_deliciosa.dto.DashboardDTO$MonthlyRevenueDTO(
                YEAR(p.fechaCreacion),
                MONTH(p.fechaCreacion),
                SUM(p.total)
            )
            FROM Pedido p
            WHERE p.estado = 'ENTREGADO'
            GROUP BY YEAR(p.fechaCreacion), MONTH(p.fechaCreacion)
            ORDER BY YEAR(p.fechaCreacion) DESC, MONTH(p.fechaCreacion) DESC
            LIMIT 12
            """)
    List<MonthlyRevenueDTO> getMonthlyRevenue();

    @Query(value = """
            SELECT new com.bances.agua_deliciosa.dto.DashboardDTO$RecentOrderDTO(
                p.cliente.nombre,
                p.producto.nombre,
                p.estado,
                p.total
            )
            FROM Pedido p
            ORDER BY p.fechaCreacion DESC
            LIMIT 4
            """)
    List<RecentOrderDTO> getRecentOrders();

    @Query(value = """
            SELECT new com.bances.agua_deliciosa.dto.DashboardDTO$TopProductDTO(
                p.producto.nombre,
                p.producto.precio,
                COUNT(p),
                0.0,
                p.producto.stock
            )
            FROM Pedido p
            WHERE p.estado = 'ENTREGADO'
            GROUP BY p.producto.id, p.producto.nombre, p.producto.precio, p.producto.stock
            ORDER BY COUNT(p) DESC
            LIMIT 5
            """)
    List<TopProductDTO> getTopProducts();

    @Query(value = """
            SELECT new com.bances.agua_deliciosa.dto.DashboardDTO$TopCustomerDTO(
                p.cliente.nombre,
                COUNT(p),
                SUM(p.total),
                MAX(p.fechaCreacion)
            )
            FROM Pedido p
            WHERE p.estado = 'ENTREGADO'
            GROUP BY p.cliente.id, p.cliente.nombre
            ORDER BY COUNT(p) DESC
            LIMIT 5
            """)
    List<TopCustomerDTO> getTopCustomers();
}
