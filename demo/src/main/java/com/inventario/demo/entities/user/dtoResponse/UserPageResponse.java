package com.inventario.demo.entities.user.dtoResponse;

import java.util.List;

public record UserPageResponse (List<UserResponseDto> users,
                                int totalPages,
                                long totalElements){
}
