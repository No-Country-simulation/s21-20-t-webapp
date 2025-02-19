package com.inventario.demo.user.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AuthCreateUserRequestDto (@NotBlank(message = "El nombre es obligatorio")
                                        @Schema(description = "Nombre del usuario", requiredMode = Schema.RequiredMode.REQUIRED, example = "Juan")
                                        String name,
                                        @NotBlank(message = "El apellido es obligatorio")
                                        @Schema(description = "Apellido del usuario",  requiredMode = Schema.RequiredMode.REQUIRED, example = "Perez")
                                        String lastName,
                                        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es valido")
                                        @Schema(description = "Correo del usuario",  requiredMode = Schema.RequiredMode.REQUIRED, example = "7o5f1@example.com")
                                        String email,
                                        @NotBlank(message = "La contraseña es obligatoria")
                                        @Schema(description = "Contraseña del usuario",  requiredMode = Schema.RequiredMode.REQUIRED, example = "password123")
                                        String password,
                                        @NotNull(message = "El telefono es obligatorio")
                                        @Schema(description = "Telefono del usuario",  requiredMode = Schema.RequiredMode.REQUIRED, example = "1234567890")
                                        Long phoneNumber,
                                        @NotBlank(message = "El pais es obligatorio")
                                        @Schema(description = "Pais del usuario",  requiredMode = Schema.RequiredMode.REQUIRED, example = "Colombia")
                                        String country,
                                        @NotNull(message = "La fecha de nacimiento es obligatoria")
                                        @Schema(description = "Fecha de nacimiento del usuario",  requiredMode = Schema.RequiredMode.REQUIRED, example = "1990-01-01T00:00:00")
                                        LocalDate birthDate,
                                        @Valid
                                        @Schema(description = "Roles del usuario",  requiredMode = Schema.RequiredMode.REQUIRED)
                                        AuthCreateRoleRequestDto roleDto){
}
