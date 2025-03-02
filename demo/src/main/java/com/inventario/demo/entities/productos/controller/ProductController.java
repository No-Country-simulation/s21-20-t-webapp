package com.inventario.demo.entities.productos.controller;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.entities.productos.dtoRequest.ProductRequestDto;
import com.inventario.demo.entities.productos.dtoResponse.ProductPageableResponse;
import com.inventario.demo.entities.productos.dtoResponse.ResponseProductRequest;
import com.inventario.demo.entities.productos.model.ProductModel;
import com.inventario.demo.entities.productos.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ProductModel> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto updatedProductDto) {
        ProductModel updatedProduct = productService.editProduct(id, updatedProductDto);
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
}
