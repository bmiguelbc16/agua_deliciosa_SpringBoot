package com.bances.agua_deliciosa.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Obtén el código de error
        Object status = request.getAttribute("javax.servlet.error.status_code");

        // Obtén el mensaje de error
        Object errorMessage = request.getAttribute("javax.servlet.error.message");

        // Obtén el tipo de excepción, si la hay
        Object exception = request.getAttribute("javax.servlet.error.exception");

        // Agregar detalles al modelo para mostrar en la vista
        model.addAttribute("status", status);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("exception", exception);

        // Puedes agregar detalles adicionales como la URL solicitada y más
        model.addAttribute("url", request.getAttribute("javax.servlet.error.request_uri"));

        return "error"; // Nombre de la plantilla Thymeleaf para mostrar el error
    }
}
