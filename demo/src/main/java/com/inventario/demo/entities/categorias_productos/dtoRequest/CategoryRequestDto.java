package com.inventario.demo.entities.categorias_productos.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record CategoryRequestDto(

        @NotBlank(message = "El nombre de la categoría es obligatorio")
        @Schema(description = "Nombre de la categoría", requiredMode = Schema.RequiredMode.REQUIRED, example = "Frutas")
        String nombre,

        @Schema(description = "Campos personalizados para la categoría", example = "{'color': 'rojo'}")
        Map<String, Object> camposPersonalizados
) {
}
