package com.bances.agua_deliciosa.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class DashboardDTO {
    private StatsDTO stats;
    private List<MonthlyRevenueDTO> monthlyRevenue;
    private List<RecentOrderDTO> recentOrders;
    private List<TopProductDTO> topProducts;
    private List<TopCustomerDTO> topCustomers;

    @Data
    public static class StatsDTO {
        private long totalCustomers;
        private long totalOrders;
        private long totalProducts;
        private long totalEmployees;
    }

    @Data
    public static class MonthlyRevenueDTO {
        private int year;
        private int month;
        private BigDecimal amount;
    }

    @Data
    public static class RecentOrderDTO {
        private String customerName;
        private String productName;
        private String status;
        private BigDecimal total;
    }

    @Data
    public static class TopProductDTO {
        private String name;
        private BigDecimal price;
        private int salesCount;
        private double salesGrowth;
        private int stock;
    }

    @Data
    public static class TopCustomerDTO {
        private String name;
        private int orderCount;
        private BigDecimal totalSpent;
        private LocalDateTime lastPurchase;
    }
}
