package com.bances.agua_deliciosa.dto.admin.summary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientSummaryDTO {
    private String name;
    private int orderCount;
    private double totalSpent;
}
