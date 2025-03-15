package com.inventario.demo.entities.categorias_productos.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    @Schema(description = "El id del tenant", example = "1")
    @NotNull(message = "El id del tenant es obligatorio")
    private Long tenantId;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Schema(description = "Nombre de la categoría", requiredMode = Schema.RequiredMode.REQUIRED, example = "Frutas")
    private String nombre;

    @Schema(description = "Campos personalizados para la categoría", example = "{\"key1\": \"value1\", \"key2\": \"value2\"}")
    private Map<String, Object> camposPersonalizados;
}
