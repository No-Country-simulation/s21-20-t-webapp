package com.inventario.demo.entities.categorias_productos.dtoResponse;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"Nombre", "CamposPersonalizados"})
public record ResponseCategoryRequest(
        String Nombre,
        Object CamposPersonalizados
) {
}