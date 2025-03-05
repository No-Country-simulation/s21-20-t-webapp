package com.inventario.demo.entities.transaction.controller;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.entities.transaction.dtoRequest.TransactionRequestDto;
import com.inventario.demo.entities.transaction.dtoRequest.TransactionUpdateRequestDto;
import com.inventario.demo.entities.transaction.dtoResponse.TransactionResponseDto;
import com.inventario.demo.entities.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
            @Valid @RequestBody TransactionUpdateRequestDto transactionRequestDto) {
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

    @Operation(
            summary = "Buscar transacciones con filtros opcionales",
            description = "Permite buscar transacciones aplicando filtros como nombre, tipo, referencia, fecha de creación y tenantId."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de transacciones paginada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<TransactionResponseDto>> searchTransactions(
            @Parameter(description = "Tipo de transacción", in = ParameterIn.QUERY)
            @RequestParam(required = false) String type,

            @Parameter(description = "Referencia de la transacción", in = ParameterIn.QUERY)
            @RequestParam(required = false) String reference,

            @Parameter(description = "Fecha de inicio para el rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Fecha final para el rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "ID del tenant", in = ParameterIn.QUERY)
            @RequestParam(required = false) Long tenantId,

            @Parameter(description = "ID del creador", in = ParameterIn.QUERY)
            @RequestParam(required = false) Long createdById,

            @Parameter(description = "ID del producto", in = ParameterIn.QUERY)
            @RequestParam(required = false) Long productId,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginatedResponse<TransactionResponseDto> response = transactionService.searchTransactions(
                type, reference, startDate, endDate, tenantId, createdById,
                productId, page, size
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener transacciones dentro de un rango de fechas",
            description = "Devuelve una lista paginada de transacciones creadas entre las fechas especificadas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de transacciones paginada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/date-range")
    public ResponseEntity<PaginatedResponse<TransactionResponseDto>> getAllTransactionsByCreatedAtRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginatedResponse<TransactionResponseDto> response = transactionService.getAllTransactionsByCreatedAtRange(
                startDate, endDate, page, size
        );
        return ResponseEntity.ok(response);
    }


}
