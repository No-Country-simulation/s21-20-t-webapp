package com.inventario.demo.entities.tenant.controller;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.entities.tenant.dtoRequest.TenantRequestDto;
import com.inventario.demo.entities.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.tenant.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tenants", description = "Endpoints para gestión de tenants")
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;


    @Operation(summary = "Obtener todos los tenants", description = "Lista todos los tenants")
    @ApiResponse(responseCode = "200", description = "Tenants obtenidos exitosamente")
    @GetMapping
    public ResponseEntity<PaginatedResponse<TenantResponseDto>> getAllTenants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginatedResponse<TenantResponseDto> response = tenantService.getAllTenants(page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener un tenant por ID", description = "Devuelve todos los detalles de un tenant por su ID.")
    @ApiResponse(responseCode = "200", description = "Tenant obtenido exitosamente")
    @ApiResponse(responseCode = "404", description = "Tenant no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<TenantResponseDto> getTenantById(@PathVariable Long id) {
        TenantResponseDto tenant = tenantService.getTenantById(id);
        return ResponseEntity.ok(tenant);
    }

    @Operation(summary = "Crear un tenant", description = "Crea un nuevo tenant")
    @ApiResponse(responseCode = "201", description = "Tenant creado exitosamente")
    @PostMapping
    public ResponseEntity<TenantResponseDto> createTenant(@Valid @RequestBody TenantRequestDto tenantRequestDto) {
        TenantResponseDto createdTenant = tenantService.createTenant(tenantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTenant);
    }

    @Operation(summary = "Actualizar un tenant", description = "Actualiza los detalles de un tenant existente por su ID")
    @ApiResponse(responseCode = "200", description = "Tenant actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Tenant no encontrado")
    @PatchMapping("/{id}")
    public ResponseEntity<TenantResponseDto> updateTenant(
            @PathVariable Long id,
            @Valid @RequestBody TenantRequestDto tenantRequestDto) {
        TenantResponseDto updatedTenant = tenantService.updateTenant(id, tenantRequestDto);
        return ResponseEntity.ok(updatedTenant);
    }

    @Operation(summary = "Eliminar un tenant", description = "Elimina un tenant por su ID")
    @ApiResponse(responseCode = "204", description = "Tenant eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Tenant no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }
}
