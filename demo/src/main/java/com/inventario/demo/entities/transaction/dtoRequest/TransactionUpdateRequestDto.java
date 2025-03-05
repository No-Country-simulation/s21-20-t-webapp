package com.inventario.demo.entities.transaction.dtoRequest;

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
@Schema(description = "DTO para actualizar una transacción")
public class TransactionUpdateRequestDto {
    @Schema(description = "Cantidad de productos en la transacción", example = "10")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
    private Integer quantity;

    @Schema(description = "El tipo de transacción que se va a realizar (ej: ENTRADA o SALIDA)", example = "ENTRADA")
    @NotNull(message = "El tipo de transacción es obligatorio")
    private String type;

    @Schema(description = "Referencia de la transacción en el sistema (ej: ORD-12345)", example = "ORD-12345")
    private String reference;

    @Schema(description = "Notas adicionales", example = "Alguna nota importante")
    private String notes;
}
