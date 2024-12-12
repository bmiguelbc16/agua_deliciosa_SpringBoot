package com.bances.agua_deliciosa.service.core;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.bances.agua_deliciosa.dto.admin.DashboardStatsDTO;
import com.bances.agua_deliciosa.repository.ClientRepository;
import com.bances.agua_deliciosa.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    
    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        // Obtener conteos reales de la base de datos
        stats.setClientCount(clientRepository.count());
        stats.setEmployeeCount(employeeRepository.count());
        
        // Por ahora, valores de ejemplo para los demás
        stats.setOrderCount(0L);
        stats.setProductCount(0L);
        stats.setSalesGrowth(0.0);
        
        return stats;
    }
}