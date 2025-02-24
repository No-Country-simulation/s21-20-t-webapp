package com.inventario.demo.transaction.controller;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.transaction.dtoRequest.TransactionRequestDto;
import com.inventario.demo.transaction.dtoResponse.TransactionResponseDto;
import com.inventario.demo.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactional")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operaciones relacionadas con las transacciones")
public class TransactionalController {

    private final TransactionService transactionService;

    @Operation(summary = "Obtiene todas las transacciones", description = "Retorna todas las transacciones existentes")
    @ApiResponse(responseCode = "200", description = "Transacciones obtenidas exitosamente")
    @GetMapping
    public ResponseEntity<PaginatedResponse<TransactionResponseDto>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginatedResponse<TransactionResponseDto> response = transactionService.getAllTransactions(page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtiene una transacción", description = "Retorna una transacción existente por ID")
    @ApiResponse(responseCode = "200", description = "Transacción obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        TransactionResponseDto transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Crear una transacción", description = "Crea una nueva transacción")
    @ApiResponse(responseCode = "201", description = "Transacción creada exitosamente")
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody TransactionRequestDto transactionRequestDto) {
        TransactionResponseDto createdTransaction = transactionService.createTransaction(transactionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @Operation(summary = "Actualizar una transacción", description = "Actualiza los detalles de una transacción existente")
    @ApiResponse(responseCode = "200", description = "Transacción actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequestDto transactionRequestDto) {
        TransactionResponseDto updatedTransaction = transactionService.updateTransaction(id, transactionRequestDto);
        return ResponseEntity.ok(updatedTransaction);
    }

    @Operation(summary = "Elimina una transacción", description = "Elimina una transacción existente")
    @ApiResponse(responseCode = "204", description = "Transacción eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }


}
