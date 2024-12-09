package com.bances.agua_deliciosa.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import com.bances.agua_deliciosa.controller.BaseController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public abstract class AdminController extends BaseController {
    // Funcionalidad común para todos los controladores de admin
    protected void logAdminAction(String action) {
        log.info("Admin action: {}", action);
    }
} 