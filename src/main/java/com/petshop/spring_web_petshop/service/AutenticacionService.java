package com.petshop.spring_web_petshop.service;

import org.springframework.stereotype.Service;

import com.petshop.spring_web_petshop.dto.AutenticacionFilter;
import com.petshop.spring_web_petshop.model.Usuario;
import com.petshop.spring_web_petshop.repository.UsuarioRepository;
import com.petshop.spring_web_petshop.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

    private final UsuarioRepository usuarioRepository;

    public Usuario autenticar(AutenticacionFilter filter) {
        if (filter.email() == null || filter.password() == null) {
            return null;
        }
        return usuarioRepository.findByEmailAndPasswordHash(filter.email(), PasswordUtil.hash(filter.password()));
    }
}
