package com.inventario.demo.inventory.dtoRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos del inventario")
public class InventoryRequestDto {

    @Schema(description = "Id del producto al que pertenece el inventario", example = "1")
    @NotNull(message = "El id del producto es obligatorio")
    private Long productId;

    @Schema(description = "Id del tenant al que pertenece el inventario", example = "1")
    @NotNull(message = "El id del tenant es obligatorio")
    private Long tenantId;

    @Schema(description = "Cantidad de productos en el inventario", example = "10")
    private Integer quantity;

    @Schema(description = "Ubicación del inventario", example = "Bodega 1")
    private String location;
}
