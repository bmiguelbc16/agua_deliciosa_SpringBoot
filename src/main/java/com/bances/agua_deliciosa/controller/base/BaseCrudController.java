package com.bances.agua_deliciosa.controller.base;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.bances.agua_deliciosa.service.core.base.BaseService;

public abstract class BaseCrudController<T, D> extends BaseController implements CrudController<T, D> {
    
    protected final BaseService<T, D> service;
    
    protected BaseCrudController(BaseService<T, D> service) {
        this.service = service;
    }
    
    @Override
    @GetMapping("")
    public String index(Model model, HttpServletRequest request) {
        addCommonAttributes(model, request);
        model.addAttribute("items", service.findAll());
        return view("index");
    }
    
    @Override
    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        addCommonAttributes(model, request);
        return view("create");
    }
    
    @Override
    @PostMapping("")
    public String store(
        @Valid @ModelAttribute D dto,
        BindingResult result,
        Model model,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addCommonAttributes(model, request);
            return view("create");
        }
        
        try {
            service.create(dto);
            addSuccessMessage(redirectAttributes, getEntityName() + " creado exitosamente");
            return redirect(getBasePath());
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al crear " + getEntityName() + ": " + e.getMessage());
            return redirect(getBasePath() + "/create");
        }
    }
    
    @Override
    @GetMapping("/{id}/edit")
    public String edit(
        @PathVariable Long id,
        Model model,
        HttpServletRequest request
    ) {
        addCommonAttributes(model, request);
        model.addAttribute("item", service.getById(id));
        return view("edit");
    }
    
    @Override
    @PutMapping("/{id}")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute D dto,
        BindingResult result,
        Model model,
        HttpServletRequest request,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            addCommonAttributes(model, request);
            return view("edit");
        }
        
        try {
            service.update(id, dto);
            addSuccessMessage(redirectAttributes, getEntityName() + " actualizado exitosamente");
            return redirect(getBasePath());
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al actualizar " + getEntityName() + ": " + e.getMessage());
            return redirect(getBasePath() + "/" + id + "/edit");
        }
    }
    
    @Override
    @DeleteMapping("/{id}")
    public String destroy(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes
    ) {
        try {
            service.delete(id);
            addSuccessMessage(redirectAttributes, getEntityName() + " eliminado exitosamente");
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, "Error al eliminar " + getEntityName() + ": " + e.getMessage());
        }
        return redirect(getBasePath());
    }
    
    protected abstract String getBasePath();
    protected abstract String getEntityName();
} 