package com.inventario.demo.entities.user.dtoResponse;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"userId", "username", "message", "jwt", "status"})
public record AuthResponseRegisterDto (Long userId,
                                      String username,
                                      String message,
                                      boolean status){
}
