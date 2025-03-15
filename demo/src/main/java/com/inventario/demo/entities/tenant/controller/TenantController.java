package com.inventario.demo.entities.tenant.controller;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.entities.tenant.dtoRequest.TenantRequestDto;
import com.inventario.demo.entities.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.entities.tenant.service.TenantService;
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
import java.util.Map;

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

    /**
     * Búsqueda avanzada de tenants por nombre, rango de fechas y filtrado por campo de configuración.
     *
     * @param name        Nombre (o parte del mismo) del tenant a buscar (opcional).
     * @param startDate   Fecha de inicio del rango de creación (opcional).
     * @param endDate     Fecha final del rango de creación (opcional).
     * @param configKey   Clave del campo de configuración a filtrar (opcional).
     * @param configValue Valor que debe tener la clave en la configuración (opcional).
     * @param page        Número de página para paginación (por defecto 0).
     * @param size        Tamaño de página para paginación (por defecto 10).
     * @return Lista paginada de tenants que cumplen los criterios.
     */
    @Operation(
            summary = "Búsqueda avanzada de tenants",
            description = "Permite buscar tenants filtrando por nombre, rango de fechas de creación y un valor específico en un campo de configuración. " +
                    "Los parámetros son opcionales y se pueden combinar para realizar busquedas avanzadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenants encontrados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos")
    })
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<TenantResponseDto>> searchTenants(
            @Parameter(description = "Nombre o parte del nombre del tenant", in = ParameterIn.QUERY)
            @RequestParam(value = "name", required = false) String name,

            @Parameter(description = "Fecha de inicio para el rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Fecha de fin para el rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "Clave del campo de configuración", in = ParameterIn.QUERY)
            @RequestParam(value = "configKey", required = false) String configKey,

            @Parameter(description = "Valor a buscar en la configuración", in = ParameterIn.QUERY)
            @RequestParam(value = "configValue", required = false) String configValue,

            @Parameter(description = "Número de página", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PaginatedResponse<TenantResponseDto> response =
                tenantService.searchTenants(name, startDate, endDate, configKey, configValue, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene tenants creados dentro de un rango de fechas.
     *
     * @param startDate Fecha de inicio para el rango de creación.
     * @param endDate   Fecha de fin para el rango de creación.
     * @param page      Número de página para paginación (por defecto 0).
     * @param size      Tamaño de página para paginación (por defecto 10).
     * @return Lista paginada de tenants creados en el rango especificado.
     */
    @Operation(
            summary = "Obtener tenants por rango de fechas de creación",
            description = "Devuelve una lista paginada de tenants que fueron creados entre las fechas indicadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tenants encontrados exitosamente"),
            @ApiResponse(responseCode = "400", description = "El rango de fechas es inválido")
    })
    @GetMapping("/date-range")
    public ResponseEntity<PaginatedResponse<TenantResponseDto>> getAllTenantsByCreationDateRange(
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

        PaginatedResponse<TenantResponseDto> response =
                tenantService.getAllTenantsByCreatedAtRange(startDate, endDate, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza únicamente la configuración del tenant.
     *
     * @param id               Identificador del tenant.
     * @param newConfiguration Nuevo mapa de configuración que se desea guardar.
     * @return Tenant actualizado con la nueva configuración.
     */
    @Operation(
            summary = "Actualizar configuración de un tenant",
            description = "Actualiza únicamente el campo de configuración del tenant identificado por su id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuración actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tenant no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de configuración inválidos")
    })
    @PatchMapping("/{id}/configuration")
    public ResponseEntity<TenantResponseDto> updateTenantConfiguration(
            @Parameter(description = "ID del tenant", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "Nuevo mapa de configuración", required = true)
            @RequestBody Map<String, Object> newConfiguration) {

        TenantResponseDto updatedTenant = tenantService.updateTenantConfiguration(id, newConfiguration);
        return ResponseEntity.ok(updatedTenant);
    }
}
