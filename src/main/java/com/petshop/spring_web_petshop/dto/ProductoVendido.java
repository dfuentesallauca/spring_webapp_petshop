package com.petshop.spring_web_petshop.dto;

import java.math.BigDecimal;

public record ProductoVendido(String nombre, String categoria, BigDecimal precio, Long totalVendido) {
}
