package com.inventario.demo.transaction.dtoRequest;

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
@Schema(description = "Datos de la transacción de inventario")
public class TransactionRequestDto {

    @Schema(description = "El id del tenant en el cual se realiza la transacción", example = "1")
    @NotNull(message = "El id del tenant es obligatorio")
    private Long tenantId;

    /*@Schema(description = "ID of the product", example = "100")
    @NotNull(message = "Product ID is required")
    private Long productId;*/

    @Schema(description = "Cantidad de productos en la transacción", example = "10")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
    private Integer quantity;

    @Schema(description = "El tipo de transacción que se va a realizar (ej: ENTRADA o SALIDA)", example = "ENTRADA")
    @NotNull(message = "Transaction type is required")
    private String type;

    @Schema(description = "Referencia de la transacción en el sistema (ej: ORD-12345)", example = "ORD-12345")
    private String reference;

    @Schema(description = "Notas adicionales", example = "Alguna nota importante")
    private String notes;

    @Schema(description = "El id del usuario que realiza la transacción", example = "1")
    @NotNull(message = "El id del usuario es obligatorio")
    private Long createdById;
}
