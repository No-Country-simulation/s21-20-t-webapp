package com.inventario.demo.entities.productos.controller;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.entities.productos.dtoRequest.ProductRequestDto;
import com.inventario.demo.entities.productos.dtoResponse.ProductPageableResponse;
import com.inventario.demo.entities.productos.dtoResponse.ResponseProductRequest;
import com.inventario.demo.entities.productos.model.ProductModel;
import com.inventario.demo.entities.productos.service.ProductService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "Product Controller")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto.")
    @ApiResponse(responseCode = "200", description = "Producto creado exitosamente")
    @PostMapping
    public ResponseEntity<ResponseProductRequest> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ResponseProductRequest product = productService.saveProduct(productRequestDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista paginada de productos.")
    @ApiResponse(responseCode = "200", description = "Productos obtenidos exitosamente")
    @GetMapping
    public ResponseEntity<PaginatedResponse<ResponseProductRequest>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponse<ResponseProductRequest> response = productService.getAllProducts(page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener un producto por ID", description = "Devuelve un producto por su ID.")
    @ApiResponse(responseCode = "200", description = "Producto obtenido exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductRequest> getProductById(@PathVariable Long id) {
        ResponseProductRequest product = productService.getUserById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar un producto", description = "Actualiza los detalles de un producto existente.")
    @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseProductRequest> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto updatedProductDto) {
        ResponseProductRequest updatedProduct = productService.updateProduct(id, updatedProductDto);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto por su ID.")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Búsqueda avanzada de productos.
     *
     * Permite filtrar productos por nombre, SKU, valores específicos en campos personalizados,
     * rango de fechas de creación, tenant, usuario creador y categoría.
     *
     * @param nombre       Nombre (o parte del nombre) del producto (opcional).
     * @param sku          SKU del producto (opcional).
     * @param configKey    Clave del campo personalizado a filtrar (opcional).
     * @param configValue  Valor que debe tener la clave en el campo personalizado (opcional).
     * @param startDate    Fecha de inicio para el rango de creación (YYYY-MM-DD) (opcional).
     * @param endDate      Fecha de fin para el rango de creación (YYYY-MM-DD) (opcional).
     * @param tenantId     Identificador del tenant (opcional).
     * @param createdBy    Identificador del usuario creador (opcional).
     * @param categoryId   Identificador de la categoría (opcional).
     * @param page         Número de página para paginación (por defecto 0).
     * @param size         Tamaño de página para paginación (por defecto 10).
     * @return Lista paginada de productos que cumplen con los criterios de búsqueda.
     */
    @Operation(
            summary = "Búsqueda avanzada de productos",
            description = "Filtra productos por nombre, SKU, configuración, rango de fechas de creación, tenant, createdBy y categoría."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos")
    })
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<ResponseProductRequest>> searchProducts(
            @Parameter(description = "Nombre o parte del nombre del producto", in = ParameterIn.QUERY)
            @RequestParam(value = "nombre", required = false) String nombre,

            @Parameter(description = "SKU del producto", in = ParameterIn.QUERY)
            @RequestParam(value = "sku", required = false) String sku,

            @Parameter(description = "Clave del campo de configuración", in = ParameterIn.QUERY)
            @RequestParam(value = "configKey", required = false) String configKey,

            @Parameter(description = "Valor del campo de configuración", in = ParameterIn.QUERY)
            @RequestParam(value = "configValue", required = false) String configValue,

            @Parameter(description = "Fecha de inicio del rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Fecha de fin del rango de creación (YYYY-MM-DD)", in = ParameterIn.QUERY)
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "ID del tenant", in = ParameterIn.QUERY)
            @RequestParam(value = "tenantId", required = false) Long tenantId,

            @Parameter(description = "ID de la categoría", in = ParameterIn.QUERY)
            @RequestParam(value = "categoryId", required = false) Long categoryId,

            @Parameter(description = "Número de página", in = ParameterIn.QUERY)
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", in = ParameterIn.QUERY)
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PaginatedResponse<ResponseProductRequest> response = productService.searchProducts(
                nombre, sku, configKey, configValue, startDate, endDate, tenantId, categoryId, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los productos creados en un rango de fechas especificado.
     *
     * @param startDate Fecha de inicio para el rango de creación (YYYY-MM-DD).
     * @param endDate   Fecha de fin para el rango de creación (YYYY-MM-DD).
     * @param page      Número de página para paginación (por defecto 0).
     * @param size      Tamaño de página para paginación (por defecto 10).
     * @return Lista paginada de productos creados entre las fechas indicadas.
     */
    @Operation(
            summary = "Obtener productos por rango de fecha de creación",
            description = "Devuelve una lista paginada de productos creados entre las fechas especificadas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos encontrados exitosamente"),
            @ApiResponse(responseCode = "400", description = "El rango de fechas es inválido")
    })
    @GetMapping("/date-range")
    public ResponseEntity<PaginatedResponse<ResponseProductRequest>> getAllTenantsByCreadoEnRange(
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

        PaginatedResponse<ResponseProductRequest> response =
                productService.getAllTenantsByCreadoEnRange(startDate, endDate, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza únicamente la configuración de un producto.
     *
     * @param id               Identificador del producto.
     * @param newConfiguration Nuevo mapa de configuración a aplicar.
     * @return Producto actualizado con la nueva configuración.
     */
    @Operation(
            summary = "Actualizar configuración de un producto",
            description = "Actualiza únicamente el campo de configuración (camposPersonalizados) del producto identificado por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuración actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de configuración inválidos")
    })
    @PatchMapping("/{id}/configuration")
    public ResponseEntity<ResponseProductRequest> updateProductConfiguration(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "Nuevo mapa de configuración", required = true)
            @RequestBody Map<String, Object> newConfiguration) {

        ResponseProductRequest updatedProduct = productService.updateProductConfiguration(id, newConfiguration);
        return ResponseEntity.ok(updatedProduct);
    }
}
