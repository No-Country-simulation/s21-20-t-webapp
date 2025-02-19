package com.inventario.demo.user.dtoResponse;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"userId", "email", "message", "jwt", "status"})
public record AuthResponseRegisterDto (Long userId,
                                      String email,
                                      String message,
                                      String jwt,
                                      boolean status){
}
