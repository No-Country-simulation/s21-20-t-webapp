package com.inventario.demo.entities.categorias_productos.controller;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.entities.categorias_productos.dtoRequest.CategoryRequestDto;
import com.inventario.demo.entities.categorias_productos.dtoRequest.CategoryUpdateRequestDto;
import com.inventario.demo.entities.categorias_productos.dtoResponse.CategoryPageableResponse;
import com.inventario.demo.entities.categorias_productos.dtoResponse.ResponseCategoryRequest;
import com.inventario.demo.entities.categorias_productos.service.CategoryService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/categorias_productos")
@Tag(name = "Categorías de Productos")
@Validated
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Crear una nueva categoría de producto")
    public ResponseEntity<ResponseCategoryRequest> createCategory(@Valid @RequestBody CategoryRequestDto requestDto) {
        return ResponseEntity.ok(categoryService.createCategory(requestDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría de producto por ID")
    public ResponseEntity<ResponseCategoryRequest> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías de productos con paginación")
    public ResponseEntity<PaginatedResponse<ResponseCategoryRequest>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(categoryService.getAllCategories(page, size));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar una categoría de producto")
    public ResponseEntity<ResponseCategoryRequest> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequestDto requestDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría de producto")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Búsqueda avanzada de categorías.
     *
     * Permite filtrar categorías por nombre, valores específicos en campos de configuración,
     * rango de fechas de creación y tenant.
     *
     * @param nombre       Nombre (o parte del mismo) de la categoría (opcional).
     * @param configKey    Clave del campo de configuración a filtrar (opcional).
     * @param configValue  Valor que debe tener la clave en el campo de configuración (opcional).
     * @param startDate    Fecha de inicio para el rango de creación (YYYY-MM-DD) (opcional).
     * @param endDate      Fecha de fin para el rango de creación (YYYY-MM-DD) (opcional).
     * @param tenantId     Identificador del tenant (opcional).
     * @param page         Número de página para paginación (por defecto 0).
     * @param size         Tamaño de página para paginación (por defecto 10).
     * @return Lista paginada de categorías que cumplen con los criterios de búsqueda.
     */
    @Operation(
            summary = "Búsqueda avanzada de categorías",
            description = "Filtra categorías por nombre, configuración, rango de fechas de creación y tenant."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías encontradas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos")
    })
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<ResponseCategoryRequest>> searchCategories(
            @Parameter(description = "Nombre o parte del nombre de la categoría", in = ParameterIn.QUERY)
            @RequestParam(value = "nombre", required = false) String nombre,

            @Parameter(description = "Clave del campo de configuración", in = ParameterIn.QUERY)
            @RequestParam(value = "configKey", required = false) String configKey,

            @Parameter(description = "Valor del campo de configuración", in = ParameterIn.QUERY)
            @RequestParam(value = "configValue", required = false) String configValue,

            @Parameter(description = "Fecha de inicio para el rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Fecha de fin para el rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "ID del tenant", in = ParameterIn.QUERY)
            @RequestParam(value = "tenantId", required = false) Long tenantId,

            @Parameter(description = "Número de página", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PaginatedResponse<ResponseCategoryRequest> response = categoryService.searchCategories(
                nombre, configKey, configValue, startDate, endDate, tenantId, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todas las categorías creadas en un rango de fechas.
     *
     * @param startDate Fecha de inicio para el rango de creación (YYYY-MM-DD).
     * @param endDate   Fecha de fin para el rango de creación (YYYY-MM-DD).
     * @param page      Número de página para paginación (por defecto 0).
     * @param size      Tamaño de página para paginación (por defecto 10).
     * @return Lista paginada de categorías creadas entre las fechas indicadas.
     */
    @Operation(
            summary = "Obtener categorías por rango de fecha de creación",
            description = "Devuelve una lista paginada de categorías creadas entre las fechas especificadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías encontradas exitosamente"),
            @ApiResponse(responseCode = "400", description = "El rango de fechas es inválido")
    })
    @GetMapping("/date-range")
    public ResponseEntity<PaginatedResponse<ResponseCategoryRequest>> getAllCategoriesByCreadoEnRange(
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

        PaginatedResponse<ResponseCategoryRequest> response =
                categoryService.getAllCategoriesByCreadoEnRange(startDate, endDate, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza únicamente la configuración de una categoría.
     *
     * @param id               Identificador de la categoría.
     * @param newConfiguration Nuevo mapa de configuración a aplicar.
     * @return Categoría actualizada con la nueva configuración.
     */
    @Operation(
            summary = "Actualizar configuración de una categoría",
            description = "Actualiza únicamente el campo de configuración de la categoría (campos personalizados) identificada por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuración actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de configuración inválidos")
    })
    @PatchMapping("/{id}/configuration")
    public ResponseEntity<ResponseCategoryRequest> updateCategoryConfiguration(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "Nuevo mapa de configuración", required = true)
            @RequestBody Map<String, Object> newConfiguration) {

        ResponseCategoryRequest updatedCategory = categoryService.updateCategoryConfiguration(id, newConfiguration);
        return ResponseEntity.ok(updatedCategory);
    }
}
