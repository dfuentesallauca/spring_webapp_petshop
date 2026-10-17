package com.petshop.spring_web_petshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.petshop.spring_web_petshop.dto.ProductoFilter;
import com.petshop.spring_web_petshop.dto.ResultadoResponse;
import com.petshop.spring_web_petshop.model.Producto;
import com.petshop.spring_web_petshop.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> search(ProductoFilter filter) {
        return productoRepository.findByFilters(filter.idCategoria());
    }

    public List<Producto> searchActivos(ProductoFilter filter) {
        return productoRepository.findActivosByCategoria(filter.idCategoria());
    }

    public Producto getOne(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Transactional
    public ResultadoResponse create(Producto producto) {
        try {
            var registrado = productoRepository.save(producto);
            return new ResultadoResponse(true, String.format("Producto con Id %d registrado", registrado.getIdProducto()));
        } catch (Exception e) {
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    @Transactional
    public ResultadoResponse update(Producto producto) {
        try {
            var registrado = productoRepository.save(producto);
            return new ResultadoResponse(true, String.format("Producto con Id %d actualizado", registrado.getIdProducto()));
        } catch (Exception e) {
            return new ResultadoResponse(false, "Hubo un error en la transacción");
        }
    }

    @Transactional
    public ResultadoResponse changeActive(Integer id) {
        var producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return new ResultadoResponse(false, "Producto no encontrado");
        }
        producto.setActivo(!producto.getActivo());
        return new ResultadoResponse(true,
                String.format("Producto con Id %d %s", id, producto.getActivo() ? "activado" : "inactivado"));
    }
}
