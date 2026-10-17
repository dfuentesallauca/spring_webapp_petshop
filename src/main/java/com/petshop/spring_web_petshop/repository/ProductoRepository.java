package com.petshop.spring_web_petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petshop.spring_web_petshop.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    List<Producto> findAllByOrderByIdProductoDesc();
    
    @Query("""
            select p from Producto p
            where 
                (p.categoria.idCategoria = :idCategoria or :idCategoria is null)
            order by
                p.idProducto desc
            """)
    List<Producto> findByFilters(
            @Param("idCategoria") Integer idCategoria
    );

    @Query("""
            select p from Producto p
            where
                p.activo = true
                and (p.categoria.idCategoria = :idCategoria or :idCategoria is null)
            order by
                p.nombre
            """)
    List<Producto> findActivosByCategoria(@Param("idCategoria") Integer idCategoria);

    @Modifying
    @Query("update Producto p set p.stock = p.stock - :cantidad where p.idProducto = :id and p.stock >= :cantidad")
    int descontarStock(@Param("id") Integer id, @Param("cantidad") Integer cantidad);
}