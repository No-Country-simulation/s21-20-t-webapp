package com.inventario.demo.entities.user.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequestDto (@NotBlank(message = "El email es obligatorio")
                                  @Schema(description = "Correo del usuario", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin@example.com")
                                  String email,
                                   @NotBlank(message = "La contraseña es obligatoria")
                                  @Schema(description = "Contraseña del usuario",requiredMode = Schema.RequiredMode.REQUIRED, example = "admin123")
                                  String password){
}
