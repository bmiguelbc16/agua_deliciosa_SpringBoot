package com.bances.agua_deliciosa.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import org.springframework.lang.NonNull;

@Component
@Slf4j
public class AuthenticationLoggingFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain) 
            throws ServletException, IOException {
        
        if (request.getRequestURI().equals("/login") && request.getMethod().equals("POST")) {
            log.debug("Intento de login detectado");
            log.debug("Email: {}", request.getParameter("email"));
            log.debug("Password length: {}", 
                request.getParameter("password") != null ? 
                request.getParameter("password").length() : 0);
        }
        
        filterChain.doFilter(request, response);
    }
} 