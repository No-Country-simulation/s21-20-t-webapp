package com.inventario.demo.transaction.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private Long tenantId;
    private Long productId;
    private Integer quantity;
    private String type;
    private String reference;
    private String notes;
    private Long createdById;
    private LocalDate createdAt;
}
