package com.petshop.spring_web_petshop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petshop.spring_web_petshop.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuarioIdUsuarioOrderByFechaDesc(Integer idUsuario);

    @Query("""
            select p from Pedido p join fetch p.usuario
            where p.fecha >= :desde and p.fecha < :hasta
            order by p.fecha desc
            """)
    List<Pedido> findByRango(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
}
