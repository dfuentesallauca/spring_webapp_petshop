package com.petshop.spring_web_petshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petshop.spring_web_petshop.dto.ResultadoResponse;
import com.petshop.spring_web_petshop.model.Categoria;
import com.petshop.spring_web_petshop.service.CategoriaService;
import com.petshop.spring_web_petshop.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping("listado")
    public String listado(Model model) {
        model.addAttribute("lstCategorias", categoriaService.getAll());
        return "categoria/listado";
    }

    @GetMapping("nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria/nuevo";
    }

    @PostMapping("registrar")
    public String registrar(@ModelAttribute Categoria categoria, RedirectAttributes flash) {
        return redirigir(categoriaService.create(categoria), flash);
    }

    @GetMapping("edicion/{id}")
    public String edicion(@PathVariable Integer id, Model model) {
        model.addAttribute("categoria", categoriaService.getOne(id));
        return "categoria/edicion";
    }

    @PostMapping("guardar")
    public String guardar(@ModelAttribute Categoria categoria, RedirectAttributes flash) {
        return redirigir(categoriaService.update(categoria), flash);
    }

    @PostMapping("cambiar-activo")
    public String cambiarEstado(@RequestParam Integer id, RedirectAttributes flash) {
        return redirigir(categoriaService.changeActive(id), flash);
    }

    private String redirigir(ResultadoResponse response, RedirectAttributes flash) {
        flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
        return "redirect:/categoria/listado";
    }
}
