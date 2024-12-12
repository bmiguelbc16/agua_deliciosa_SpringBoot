package com.bances.agua_deliciosa.controller.base;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

public interface CrudController<T, D> {
    
    @GetMapping("")
    String index(Model model, HttpServletRequest request);
    
    @GetMapping("/create")
    String create(Model model, HttpServletRequest request);
    
    @PostMapping("")
    String store(@Valid @ModelAttribute D dto, 
                BindingResult result,
                Model model, 
                HttpServletRequest request,
                RedirectAttributes redirectAttributes);
    
    @GetMapping("/{id}/edit")
    String edit(@PathVariable Long id,
                Model model,
                HttpServletRequest request);
    
    @PutMapping("/{id}")
    String update(@PathVariable Long id,
                @Valid @ModelAttribute D dto,
                BindingResult result,
                Model model,
                HttpServletRequest request,
                RedirectAttributes redirectAttributes);
    
    @DeleteMapping("/{id}")
    String destroy(@PathVariable Long id,
                RedirectAttributes redirectAttributes);
} 