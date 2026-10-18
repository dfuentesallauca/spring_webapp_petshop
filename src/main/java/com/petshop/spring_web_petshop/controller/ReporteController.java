package com.petshop.spring_web_petshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.petshop.spring_web_petshop.dto.VentasFilter;
import com.petshop.spring_web_petshop.service.PedidoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("reporte")
@RequiredArgsConstructor
public class ReporteController {

    private final PedidoService pedidoService;

    @GetMapping("ventas")
    public String ventas(@ModelAttribute VentasFilter filter, Model model) {
        model.addAttribute("filter", filter);
        if (filter.desde() != null && filter.hasta() != null) {
            model.addAttribute("lstPedidos", pedidoService.getByRango(filter.desde(), filter.hasta()));
        }
        return "reporte/ventas";
    }

    @GetMapping("top-productos")
    public String topProductos(Model model) {
        model.addAttribute("lstTop", pedidoService.getTopProductos());
        return "reporte/top-productos";
    }
}
