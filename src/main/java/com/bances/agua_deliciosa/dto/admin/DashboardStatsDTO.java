package com.bances.agua_deliciosa.dto.admin;

import lombok.Data;

@Data
public class DashboardStatsDTO {
    private Long clientCount = 0L;
    private Long orderCount = 0L;
    private Long productCount = 0L;
    private Long employeeCount = 0L;
    private Double salesGrowth = 0.0;
} 