package com.petshop.spring_web_petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petshop.spring_web_petshop.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findByActivoTrueOrderByNombre();
}
