package com.petshop.spring_web_petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.petshop.spring_web_petshop.dto.ProductoVendido;
import com.petshop.spring_web_petshop.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    @Query("""
            select new com.petshop.spring_web_petshop.dto.ProductoVendido(
                pr.nombre, c.nombre, pr.precio, sum(d.cantidad))
            from DetallePedido d join d.producto pr join pr.categoria c
            group by pr.idProducto, pr.nombre, c.nombre, pr.precio
            order by sum(d.cantidad) desc
            """)
    List<ProductoVendido> findTopProductos();
}
