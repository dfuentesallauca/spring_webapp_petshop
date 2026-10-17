package com.petshop.spring_web_petshop.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.petshop.spring_web_petshop.dto.ResultadoResponse;
import com.petshop.spring_web_petshop.model.Usuario;
import com.petshop.spring_web_petshop.repository.UsuarioRepository;
import com.petshop.spring_web_petshop.util.PasswordUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll(Sort.by("nombre"));
    }

    public Usuario getOne(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public ResultadoResponse create(Usuario usuario, String password) {
        try {
            usuario.setPasswordHash(PasswordUtil.hash(password));
            var registrado = usuarioRepository.saveAndFlush(usuario);
            return new ResultadoResponse(true, String.format("Usuario con Id %d registrado", registrado.getIdUsuario()));
        } catch (Exception e) {
            return new ResultadoResponse(false, "No se pudo registrar el usuario, verifique que el correo no esté en uso");
        }
    }

    public ResultadoResponse update(Usuario datos) {
        try {
            var usuario = usuarioRepository.findById(datos.getIdUsuario()).orElse(null);
            if (usuario == null) {
                return new ResultadoResponse(false, "Usuario no encontrado");
            }
            usuario.setNombre(datos.getNombre());
            usuario.setEmail(datos.getEmail());
            usuario.setRol(datos.getRol());
            usuarioRepository.saveAndFlush(usuario);
            return new ResultadoResponse(true, String.format("Usuario con Id %d actualizado", usuario.getIdUsuario()));
        } catch (Exception e) {
            return new ResultadoResponse(false, "No se pudo actualizar el usuario, verifique que el correo no esté en uso");
        }
    }

    @Transactional
    public ResultadoResponse changeActive(Integer id) {
        var usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return new ResultadoResponse(false, "Usuario no encontrado");
        }
        usuario.setActivo(!usuario.getActivo());
        return new ResultadoResponse(true,
                String.format("Usuario con Id %d %s", id, usuario.getActivo() ? "activado" : "inactivado"));
    }
}
