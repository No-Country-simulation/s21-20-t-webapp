package com.inventario.demo.entities.productos.dtoResponse;

import java.util.List;

public record ProductPageableResponse(List<ResponseProductRequest> dtos,
                                      int totalPages,
                                      long totalElements) {
}
