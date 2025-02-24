package com.inventario.demo.registration;

import com.inventario.demo.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.user.dtoResponse.AuthResponseRegisterDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponseDto {
    private AuthResponseRegisterDto authResponse;
    private TenantResponseDto tenantResponse;
}
