package com.inventario.demo.registration;

import com.inventario.demo.entities.tenant.dtoRequest.TenantRequestDto;
import com.inventario.demo.entities.user.dtoRequest.AuthCreateUserRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de registro de un nuevo usuario y tenant")
public class RegistrationRequestDto {
    @Schema(description = "Datos del tenant")
    @NotNull(message = "La información del tenant es obligatoria")
    @Valid
    private TenantRequestDto tenant;

    @Schema(description = "Datos del usuario")
    @NotNull(message = "La información del usuario es obligatoria")
    @Valid
    private AuthCreateUserRequestDto user;

}
