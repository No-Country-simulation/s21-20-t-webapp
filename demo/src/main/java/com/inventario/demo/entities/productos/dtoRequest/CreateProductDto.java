package com.inventario.demo.entities.productos.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.Objects;

public record CreateProductDto(


        @NotBlank(message = "El nombre es obligatorio")
        @Schema(description = "Nombre del producto", requiredMode = Schema.RequiredMode.REQUIRED, example = "Fresa")
        String nombre,

        @NotBlank(message = "Todos los productos deben tener categoria")
        @Schema(description = "Categoria del producto", requiredMode = Schema.RequiredMode.REQUIRED, example = "Frutas")
        String categoria,

        @NotBlank(message = "sku")
        @Schema(description = "Codigo unico de identificacion", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345678")
        String sku,

        @Schema(description = "---", requiredMode = Schema.RequiredMode.REQUIRED, example = "---")
        Map<String, Object> camposPersonalizados
) {
}
