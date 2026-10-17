package com.petshop.spring_web_petshop.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.petshop.spring_web_petshop.dto.ResultadoResponse;
import com.petshop.spring_web_petshop.model.Categoria;
import com.petshop.spring_web_petshop.repository.CategoriaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> getAll() {
        return categoriaRepository.findAll(Sort.by("nombre"));
    }

    public List<Categoria> getActivas() {
        return categoriaRepository.findByActivoTrueOrderByNombre();
    }

    public Categoria getOne(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Transactional
    public ResultadoResponse create(Categoria categoria) {
        try {
            var registrada = categoriaRepository.save(categoria);
            return new ResultadoResponse(true, String.format("Categoría con Id %d registrada", registrada.getIdCategoria()));
        } catch (Exception e) {
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    @Transactional
    public ResultadoResponse update(Categoria categoria) {
        try {
            var actualizada = categoriaRepository.save(categoria);
            return new ResultadoResponse(true, String.format("Categoría con Id %d actualizada", actualizada.getIdCategoria()));
        } catch (Exception e) {
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    @Transactional
    public ResultadoResponse changeActive(Integer id) {
        var categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            return new ResultadoResponse(false, "Categoría no encontrada");
        }
        categoria.setActivo(!categoria.getActivo());
        return new ResultadoResponse(true,
                String.format("Categoría con Id %d %s", id, categoria.getActivo() ? "activada" : "inactivada"));
    }
}
