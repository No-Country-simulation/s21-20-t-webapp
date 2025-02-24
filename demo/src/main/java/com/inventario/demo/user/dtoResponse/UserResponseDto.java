package com.inventario.demo.user.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String lastName;
    private Long phoneNumber;
    private LocalDate birthDate;
    private Long tenantId;
    private LocalDate registerDate;
    private LocalDate lastLogin;
}
