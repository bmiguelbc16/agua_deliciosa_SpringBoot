package com.bances.agua_deliciosa.model;

import java.util.ArrayList;
import java.util.List;

public class ProductOutput extends BaseModel {
    private Employee employee;
    private String description;
    private List<ProductOutputDetail> details = new ArrayList<>();

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProductOutputDetail> getDetails() {
        return new ArrayList<>(details);
    }

    public void setDetails(List<ProductOutputDetail> details) {
        this.details = details != null ? new ArrayList<>(details) : new ArrayList<>();
    }

    public void addDetail(ProductOutputDetail detail) {
        if (detail != null) {
            details.add(detail);
            detail.setOutput(this);
        }
    }
}
