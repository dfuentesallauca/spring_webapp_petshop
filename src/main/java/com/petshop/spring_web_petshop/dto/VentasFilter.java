package com.petshop.spring_web_petshop.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public record VentasFilter(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
}
