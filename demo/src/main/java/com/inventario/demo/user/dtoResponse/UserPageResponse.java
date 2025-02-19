package com.inventario.demo.user.dtoResponse;

import com.inventario.demo.user.dtoRequest.UserRequestDto;

import java.util.List;

public record UserPageResponse (List<UserResponseDto> users,
                                int totalPages,
                                long totalElements){
}
