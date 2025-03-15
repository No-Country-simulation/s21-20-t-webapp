package com.inventario.demo.inventory.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDto {

    private Long id;
    private Long productId;
    private Long tenantId;
    private Integer quantity;
    private String location;
}
