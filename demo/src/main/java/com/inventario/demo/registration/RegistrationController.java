package com.inventario.demo.registration;

import com.inventario.demo.user.dtoResponse.UserCreationResult;
import com.inventario.demo.user.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Controlador de registro")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Operation(summary = "Registra un nuevo tenant y usuario", description = "Registra un nuevo tenant y usuario al mismo tiempo")
    @ApiResponse(responseCode = "200", description = "Tenant y usuario registrados exitosamente")
    @PostMapping
    public ResponseEntity<RegistrationResponseDto> register(@Valid @RequestBody RegistrationRequestDto registrationRequestDto) {
        RegistrationResponseDto registrationResponseDto = registrationService.register(registrationRequestDto);
        String token = registrationResponseDto.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        RegistrationResponseDto responseWithoutToken = new RegistrationResponseDto(
                registrationResponseDto.getAuthResponse(),
                registrationResponseDto.getTenantResponse(),
                null
        );


        return new ResponseEntity<>(responseWithoutToken, headers, HttpStatus.OK);
    }
}
