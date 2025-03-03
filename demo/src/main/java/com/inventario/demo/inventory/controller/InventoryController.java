package com.inventario.demo.inventory.controller;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.inventory.dtoRequest.InventoryRequestDto;
import com.inventario.demo.inventory.dtoResponse.InventoryResponseDto;
import com.inventario.demo.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Operaciones relacionadas con el inventario")
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "Obtiene todas los inventarios", description = "Retorna todas los inventarios existentes")
    @ApiResponse(responseCode = "200", description = "Inventarios obtenidas exitosamente")
    @GetMapping
    public ResponseEntity<PaginatedResponse<InventoryResponseDto>> getAllInventories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponse<InventoryResponseDto> response = inventoryService.getAllInventories(page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtiene un inventario", description = "Retorna un inventario existente por ID")
    @ApiResponse(responseCode = "200", description = "Inventario obtenido exitosamente")
    @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponseDto> getInventoryById(@PathVariable Long id) {
        InventoryResponseDto inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }


    @Operation(summary = "Crea un inventario", description = "Crea un nuevo inventario")
    @ApiResponse(responseCode = "201", description = "Inventario creado exitosamente")
    @PostMapping
    public ResponseEntity<InventoryResponseDto> createInventory(
            @Valid @RequestBody InventoryRequestDto inventoryRequestDto) {

        InventoryResponseDto inventory = inventoryService.createInventory(inventoryRequestDto);
        return ResponseEntity.ok(inventory);
    }

    @Operation(summary = "Actualiza un inventario", description = "Actualiza un inventario existente por ID")
    @ApiResponse(responseCode = "200", description = "Inventario actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    @PatchMapping("/{id}")
    public ResponseEntity<InventoryResponseDto> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequestDto inventoryRequestDto) {

        InventoryResponseDto inventory = inventoryService.updateInventory(id, inventoryRequestDto);
        return ResponseEntity.ok(inventory);
    }

    @Operation(summary = "Elimina un inventario", description = "Elimina un inventario existente por ID")
    @ApiResponse(responseCode = "204", description = "Inventario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Búsqueda avanzada de inventario.
     *
     * Permite filtrar el inventario por ubicación, rango de fechas de creación, tenant y producto.
     *
     * @param location  Ubicación del inventario (opcional).
     * @param startDate Fecha de inicio para el rango de creación (YYYY-MM-DD) (opcional).
     * @param endDate   Fecha de fin para el rango de creación (YYYY-MM-DD) (opcional).
     * @param tenantId  Identificador del tenant (opcional).
     * @param productId Identificador del producto (opcional).
     * @param page      Número de página para paginación (por defecto 0).
     * @param size      Tamaño de página para paginación (por defecto 10).
     * @return Lista paginada de inventario que cumplen con los criterios de búsqueda.
     */
    @Operation(
            summary = "Búsqueda avanzada de inventario",
            description = "Filtra inventarios por ubicación, rango de fechas de creación, tenant y producto."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventarios encontrados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos")
    })
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<InventoryResponseDto>> searchInventory(
            @Parameter(description = "Ubicación del inventario", in = ParameterIn.QUERY)
            @RequestParam(value = "location", required = false) String location,

            @Parameter(description = "Fecha de inicio para el rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Fecha de fin para el rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "ID del tenant", in = ParameterIn.QUERY)
            @RequestParam(value = "tenantId", required = false) Long tenantId,

            @Parameter(description = "ID del producto", in = ParameterIn.QUERY)
            @RequestParam(value = "productId", required = false) Long productId,

            @Parameter(description = "Número de página", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PaginatedResponse<InventoryResponseDto> response =
                inventoryService.searchInventory(location, startDate, endDate, tenantId, productId, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los inventarios creados en un rango de fechas.
     *
     * @param startDate Fecha de inicio para el rango de creación (YYYY-MM-DD).
     * @param endDate   Fecha de fin para el rango de creación (YYYY-MM-DD).
     * @param page      Número de página para paginación (por defecto 0).
     * @param size      Tamaño de página para paginación (por defecto 10).
     * @return Lista paginada de inventarios creados entre las fechas indicadas.
     */
    @Operation(
            summary = "Obtener inventarios por rango de fecha de creación",
            description = "Devuelve una lista paginada de inventarios creados entre las fechas especificadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventarios encontrados exitosamente"),
            @ApiResponse(responseCode = "400", description = "El rango de fechas es inválido")
    })
    @GetMapping("/date-range")
    public ResponseEntity<PaginatedResponse<InventoryResponseDto>> getAllInventoriesByCreatedAtRange(
            @Parameter(description = "Fecha de inicio para el rango (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Fecha de fin para el rango (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "Número de página", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PaginatedResponse<InventoryResponseDto> response =
                inventoryService.getAllInventoriesByCreatedAtRange(startDate, endDate, page, size);
        return ResponseEntity.ok(response);
    }


}
