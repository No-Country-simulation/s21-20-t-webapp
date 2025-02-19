package com.inventario.demo.user.dtoRequest;

import jakarta.validation.constraints.Size;

import java.util.List;

public record AuthCreateRoleRequestDto (@Size(max = 1, message = "roles must be a maximum of 1 character in length" ) List<String> roles){
}
