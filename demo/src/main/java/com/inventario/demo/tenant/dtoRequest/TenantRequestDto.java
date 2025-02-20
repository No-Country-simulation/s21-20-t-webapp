package com.inventario.demo.tenant.dtoRequest;

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
@Schema(description = "Request para crear un tenant")
public class TenantRequestDto {
    @Schema(description = "Nombre del tenant", example = "Mi Empresa")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Configuración del tenant", example = "{\"key1\": \"value1\", \"key2\": \"value2\"}")
    @NotNull(message = "La configuración es obligatoria")
    private Map<String, Object> configuration;
}
