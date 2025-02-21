package com.inventario.demo.entities.productos.dtoResponse;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"Nombre","Categoria","SKU"})
public record ResponseCreateProductDto(
        String Nombre,
        String Categoria,
        String SKU

) {}
