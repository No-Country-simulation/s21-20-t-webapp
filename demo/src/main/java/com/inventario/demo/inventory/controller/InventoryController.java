package com.inventario.demo.inventory.controller;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.inventory.dtoRequest.InventoryRequestDto;
import com.inventario.demo.inventory.dtoResponse.InventoryResponseDto;
import com.inventario.demo.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
