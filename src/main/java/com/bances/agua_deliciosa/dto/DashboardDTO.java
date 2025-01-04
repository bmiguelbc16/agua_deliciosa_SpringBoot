package com.bances.agua_deliciosa.dto;

import java.util.List;
import lombok.Data;

@Data
public class DashboardDTO {
    private DashboardStats stats;
    private List<OrderDTO> recentOrders;
    private List<ProductDTO> topProducts;
    private List<CustomerDTO> topCustomers;
    
    @Data
    public static class OrderDTO {
        private String customerName;
        private double total;
        private String status;
        private String statusClass;
    }
    
    @Data
    public static class ProductDTO {
        private String name;
        private double price;
        private int soldCount;
    }
    
    @Data
    public static class CustomerDTO {
        private String name;
        private int orderCount;
        private double totalSpent;
    }
}
