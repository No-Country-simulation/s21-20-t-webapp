package com.inventario.demo.inventory.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Actualizar inventario")
public class InventoryUpdateRequestDto {
    @Schema(description = "Cantidad de productos en el inventario", example = "10")
    private Integer quantity;

    @Schema(description = "Ubicación del inventario", example = "Bodega 1")
    private String location;
}
