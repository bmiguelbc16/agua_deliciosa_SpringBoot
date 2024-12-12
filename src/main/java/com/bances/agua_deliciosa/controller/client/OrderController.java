package com.bances.agua_deliciosa.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.bances.agua_deliciosa.service.auth.SecurityService;
import com.bances.agua_deliciosa.service.core.OrderService;
import com.bances.agua_deliciosa.dto.client.OrderDTO;
import jakarta.validation.Valid;

@Controller("clientOrderController")
@RequestMapping("/client/orders")
public class OrderController extends ClientController {
    
    private final OrderService orderService;
    
    public OrderController(
        SecurityService securityService,
        OrderService orderService
    ) {
        super(securityService);
        this.orderService = orderService;
    }
    
    @GetMapping
    public String index(Model model) {
        setupCommonAttributes(model);
        model.addAttribute("pageTitle", "Mis Pedidos");
        model.addAttribute("orders", orderService.getCurrentClientOrders());
        return getClientView("orders/index");
    }
    
    @GetMapping("/create")
    public String create(Model model) {
        setupCommonAttributes(model);
        model.addAttribute("pageTitle", "Nuevo Pedido");
        model.addAttribute("orderDTO", new OrderDTO());
        return getClientView("orders/create");
    }
    
    @PostMapping
    public String store(
        @Valid @ModelAttribute OrderDTO orderDTO,
        BindingResult result,
        RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return getClientView("orders/create");
        }
        
        try {
            orderService.createOrder(orderDTO);
            addSuccessMessage(redirectAttributes, "Pedido creado exitosamente");
            return "redirect:/client/orders";
        } catch (Exception e) {
            addErrorMessage(redirectAttributes, e.getMessage());
            return "redirect:/client/orders/create";
        }
    }
    
    @Override
    protected String getMenuActive() {
        return "orders";
    }
} 