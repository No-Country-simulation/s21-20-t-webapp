package com.inventario.demo.registration;

import com.inventario.demo.tenant.dtoRequest.TenantRequestDto;
import com.inventario.demo.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.tenant.service.TenantService;
import com.inventario.demo.user.dtoRequest.AuthCreateUserRequestDto;
import com.inventario.demo.user.dtoResponse.AuthResponseRegisterDto;
import com.inventario.demo.user.dtoResponse.UserCreationResult;
import com.inventario.demo.user.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final TenantService tenantService;
    private final UserDetailsServiceImpl userService;

    @Transactional
    public RegistrationResponseDto register(RegistrationRequestDto registrationRequestDto) {
        // 1. Crear el tenant utilizando el TenantRequestDto
        TenantRequestDto tenantDto = registrationRequestDto.getTenant();
        TenantResponseDto tenantResponse = tenantService.createTenant(tenantDto);

        // 2. Crear el usuario administrador asociado al tenant recién creado
        AuthCreateUserRequestDto userDto = registrationRequestDto.getUser();
        UserCreationResult authResponse = userService.createUser(userDto, tenantResponse.getId());


        // 3. Retornar la respuesta compuesta: datos de autenticación y del tenant
        return new RegistrationResponseDto(authResponse, tenantResponse, authResponse.getToken());
    }
}
