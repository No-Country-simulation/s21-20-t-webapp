package com.inventario.demo.entities.categorias_productos.dtoResponse;

import java.util.List;

public record CategoryPageableResponse(
        List<ResponseCategoryRequest> dtos,
        int totalPages,
        long totalElements
) {
}