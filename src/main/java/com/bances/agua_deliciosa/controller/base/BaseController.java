package com.bances.agua_deliciosa.controller.base;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BaseController con funcionalidades adicionales y mejor manejo de errores
 */
@Slf4j
public abstract class BaseController {
    
    /**
     * Agrega atributos comunes a la vista
     * 
     * @param model Modelo de la vista
     * @param request Solicitud HTTP
     */
    protected void addCommonAttributes(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentPath", request.getRequestURI());
        model.addAttribute("currentUser", getCurrentUser());
        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("userRoles", getUserRoles());
            model.addAttribute("isAuthenticated", true);
        }
    }
    
    /**
     * Agrega un mensaje de éxito a la respuesta de redirección
     * 
     * @param redirectAttributes Atributos de redirección
     * @param message Mensaje de éxito
     */
    protected void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("success", message);
        log.info("Success: {}", message);
    }
    
    /**
     * Agrega un mensaje de error a la respuesta de redirección
     * 
     * @param redirectAttributes Atributos de redirección
     * @param message Mensaje de error
     */
    protected void addErrorMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("error", message);
        log.error("Error: {}", message);
    }

    /**
     * Agrega un mensaje de advertencia a la respuesta de redirección
     * 
     * @param redirectAttributes Atributos de redirección
     * @param message Mensaje de advertencia
     */
    protected void addWarningMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("warning", message);
        log.warn("Warning: {}", message);
    }

    /**
     * Agrega un mensaje de información a la respuesta de redirección
     * 
     * @param redirectAttributes Atributos de redirección
     * @param message Mensaje de información
     */
    protected void addInfoMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("info", message);
        log.info("Info: {}", message);
    }
    
    /**
     * Obtiene el nombre del usuario actual
     * 
     * @return Nombre del usuario actual
     */
    protected String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() ? auth.getName() : null;
    }

    /**
     * Obtiene los roles del usuario actual
     * 
     * @return Lista de roles del usuario actual
     */
    protected List<String> getUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * Devuelve la ruta completa de la vista
     * 
     * @param view Nombre de la vista
     * @return Ruta completa de la vista
     */
    protected String view(String view) {
        return getViewPrefix() + "/" + view;
    }
    
    /**
     * Verifica si el usuario tiene un rol específico
     * 
     * @param role Rol a verificar
     * @return True si el usuario tiene el rol, false en caso contrario
     */
    protected boolean hasRole(String role) {
        return getUserRoles().contains(role);
    }

    /**
     * Verifica si el usuario tiene alguno de los roles especificados
     * 
     * @param roles Roles a verificar
     * @return True si el usuario tiene alguno de los roles, false en caso contrario
     */
    protected boolean hasAnyRole(String... roles) {
        List<String> userRoles = getUserRoles();
        for (String role : roles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtiene la ruta de redirección
     * 
     * @param path Ruta de redirección
     * @return Ruta completa de redirección
     */
    protected String redirect(String path) {
        return "redirect:" + (path.startsWith("/") ? path : "/" + path);
    }

    /**
     * Redirecciona con un mensaje de error
     * 
     * @param path Ruta de redirección
     * @param redirectAttributes Atributos de redirección
     * @param error Mensaje de error
     * @return Ruta completa de redirección
     */
    protected String redirectWithError(String path, RedirectAttributes redirectAttributes, String error) {
        addErrorMessage(redirectAttributes, error);
        return redirect(path);
    }

    /**
     * Maneja los errores de validación
     * 
     * @param result Resultado de la validación
     * @param model Modelo de la vista
     * @return Ruta de redirección o null si no hay errores
     */
    protected String handleValidationErrors(BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor corrija los errores en el formulario");
            result.getAllErrors().forEach(error -> 
                log.debug("Validation error: {}", error.getDefaultMessage())
            );
            return null;
        }
        return "";
    }

    /**
     * Registra una acción realizada por el usuario
     * 
     * @param action Acción realizada
     * @param details Detalles de la acción
     */
    protected void logAction(String action, String details) {
        String user = getCurrentUser();
        log.info("User [{}] performed action [{}]: {}", user, action, details);
    }
    
    /**
     * Obtiene el prefijo de la vista
     * Este método debe ser implementado por las clases hijas
     * 
     * @return Prefijo de la vista
     */
    protected abstract String getViewPrefix();
}