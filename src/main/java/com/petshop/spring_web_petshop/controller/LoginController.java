package com.petshop.spring_web_petshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petshop.spring_web_petshop.dto.AutenticacionFilter;
import com.petshop.spring_web_petshop.service.AutenticacionService;
import com.petshop.spring_web_petshop.util.Alert;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AutenticacionService autenticacionService;

    @GetMapping("/")
    public String root(HttpSession session, Model model) {
        if (session.getAttribute("rol") != null) {
            return "redirect:/home/dashboard";
        }
        model.addAttribute("filter", new AutenticacionFilter(null, null));
        return "login";
    }

    @PostMapping("iniciar-sesion")
    public String iniciarSesion(@ModelAttribute AutenticacionFilter filter, RedirectAttributes flash,
            HttpSession session) {

        var usuario = autenticacionService.autenticar(filter);

        if (usuario == null) {
            flash.addFlashAttribute("alert", Alert.sweetAlertError("Email y/o contraseña inválidos"));
            return "redirect:/";
        }

        if (!usuario.getActivo()) {
            flash.addFlashAttribute("alert", Alert.sweetAlertError("Usuario inactivado, comunicarse con soporte"));
            return "redirect:/";
        }

        session.setAttribute("idUsuario", usuario.getIdUsuario());
        session.setAttribute("fullName", usuario.getFullName());
        session.setAttribute("rol", usuario.getRol());

        flash.addFlashAttribute("alert", Alert.sweetToast("Bienvenido, " + usuario.getFullName(), "success", 3000));
        return "redirect:/home/dashboard";
    }

    @GetMapping("cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
