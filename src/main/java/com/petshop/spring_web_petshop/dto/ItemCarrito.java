package com.petshop.spring_web_petshop.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ItemCarrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer idProducto;
    private final String nombre;
    private final BigDecimal precio;
    @Setter
    private int cantidad;

    public BigDecimal getSubtotal() {
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }
}
