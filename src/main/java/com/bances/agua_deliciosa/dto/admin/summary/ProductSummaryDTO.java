package com.bances.agua_deliciosa.dto.admin.summary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSummaryDTO {
    private String name;
    private double price;
    private int soldCount;
}
