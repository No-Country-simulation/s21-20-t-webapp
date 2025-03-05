package com.inventario.demo.entities.productos.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequestDto {
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del producto", requiredMode = Schema.RequiredMode.REQUIRED, example = "Fresa")
    private String nombre;


    @NotBlank(message = "El sku es obligatorio")
    @Schema(description = "Codigo unico de identificacion", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    private String sku;

    @Schema(description = "Campos personalizados", requiredMode = Schema.RequiredMode.REQUIRED, example = "{\"key1\": \"value1\", \"key2\": \"value2\"}")
    private Map<String, Object> camposPersonalizados;
}
