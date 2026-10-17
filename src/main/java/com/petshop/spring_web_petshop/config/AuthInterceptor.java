package com.petshop.spring_web_petshop.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        var session = request.getSession(false);
        var rol = session != null ? (String) session.getAttribute("rol") : null;
        var ctx = request.getContextPath();

        if (rol == null) {
            response.sendRedirect(ctx + "/");
            return false;
        }

        var uri = request.getRequestURI().substring(ctx.length());
        var esCliente = uri.startsWith("/tienda");
        var esAdmin = !esCliente && !uri.startsWith("/home");

        if (esAdmin && !"ADMIN".equals(rol) || esCliente && !"CLIENTE".equals(rol)) {
            response.sendRedirect(ctx + "/home/dashboard");
            return false;
        }
        return true;
    }
}
