package com.inventario.demo.entities.user.controller;

import com.inventario.demo.entities.user.dtoRequest.AuthLoginRequestDto;
import com.inventario.demo.entities.user.dtoResponse.AuthResponseDto;
import com.inventario.demo.entities.user.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication API")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Operation(summary = "Iniciar sesión", description = "Inicia sesión y obtiene un token de autenticación.")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa")
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthLoginRequestDto authDto) {
        AuthResponseDto response = this.userDetailsServiceImpl.loginUser(authDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.token())
                .header("user-id", response.id().toString())
                .body(response);
    }


//    @Operation(summary = "Registrar nuevo usuario", description = """
//            Registra un nuevo usuario y obtiene un token de autenticación.\s
//            Los roles del usuario deben ser 'USER' o 'ADMIN'. No se puede registrar otro tipo de rol.\s
//            Todos los campos son obligatorios excepto la foto.
//            El email debe ser único.
//            """)
//    @PostMapping(value = "/register")
//    public ResponseEntity<AuthResponseRegisterDto> register(@RequestBody @Valid AuthCreateUserRequestDto authCreateUserDto) {
//        AuthResponseRegisterDto response = userDetailsServiceImpl.createUser(authCreateUserDto, null);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
}
