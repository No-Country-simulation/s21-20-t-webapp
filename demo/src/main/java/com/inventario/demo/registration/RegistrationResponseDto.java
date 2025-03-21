package com.inventario.demo.registration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventario.demo.entities.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.entities.user.dtoResponse.UserCreationResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponseDto {
    private UserCreationResult authResponse;
    private TenantResponseDto tenantResponse;
    @JsonIgnore
    private String token;
}
