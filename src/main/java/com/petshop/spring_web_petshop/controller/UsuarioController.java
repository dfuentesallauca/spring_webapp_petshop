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
import com.petshop.spring_web_petshop.model.Usuario;
import com.petshop.spring_web_petshop.service.UsuarioService;
import com.petshop.spring_web_petshop.util.Alert;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("listado")
    public String listado(Model model) {
        model.addAttribute("lstUsuarios", usuarioService.getAll());
        return "usuario/listado";
    }

    @GetMapping("nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/nuevo";
    }

    @PostMapping("registrar")
    public String registrar(@ModelAttribute Usuario usuario, @RequestParam String password, RedirectAttributes flash) {
        return redirigir(usuarioService.create(usuario, password), flash);
    }

    @GetMapping("edicion/{id}")
    public String edicion(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", usuarioService.getOne(id));
        return "usuario/edicion";
    }

    @PostMapping("guardar")
    public String guardar(@ModelAttribute Usuario usuario, RedirectAttributes flash) {
        return redirigir(usuarioService.update(usuario), flash);
    }

    @PostMapping("cambiar-activo")
    public String cambiarEstado(@RequestParam Integer id, RedirectAttributes flash) {
        return redirigir(usuarioService.changeActive(id), flash);
    }

    private String redirigir(ResultadoResponse response, RedirectAttributes flash) {
        flash.addFlashAttribute("alert", Alert.sweetToast(response.mensaje(), response.success() ? "success" : "error", 5000));
        return "redirect:/usuario/listado";
    }
}
