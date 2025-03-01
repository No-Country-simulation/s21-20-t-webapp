package com.inventario.demo.entities.transaction.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPageResponse {
    private List<TransactionResponseDto> transactions;
    private int totalPages;
    private long totalElements;
}
