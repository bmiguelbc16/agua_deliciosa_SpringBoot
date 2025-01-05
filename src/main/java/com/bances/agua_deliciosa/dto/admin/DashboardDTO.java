package com.bances.agua_deliciosa.dto.admin;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

import com.bances.agua_deliciosa.dto.admin.summary.OrderSummaryDTO;
import com.bances.agua_deliciosa.dto.admin.summary.ProductSummaryDTO;
import com.bances.agua_deliciosa.dto.admin.summary.ClientSummaryDTO;

@Data
@Builder
public class DashboardDTO {
    private long totalClients;
    private long totalEmployees;
    private long totalOrders;
    private long totalProducts;
    private long pendingOrders;
    private long inDeliveryOrders;
    private List<OrderSummaryDTO> recentOrders;
    private List<ProductSummaryDTO> topProducts;
    private List<ClientSummaryDTO> topCustomers;
    
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalClients", totalClients);
        stats.put("totalEmployees", totalEmployees);
        stats.put("totalOrders", totalOrders);
        stats.put("totalProducts", totalProducts);
        stats.put("pendingOrders", pendingOrders);
        stats.put("inDeliveryOrders", inDeliveryOrders);
        return stats;
    }
}
