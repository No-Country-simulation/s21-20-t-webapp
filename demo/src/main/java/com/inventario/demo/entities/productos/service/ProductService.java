package com.inventario.demo.entities.productos.service;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.config.exceptions.CategoryNotFoundException;
import com.inventario.demo.config.exceptions.ProductNotFoundException;
import com.inventario.demo.config.exceptions.ResourceNotFoundException;
import com.inventario.demo.config.exceptions.TenantNotFoundException;
import com.inventario.demo.entities.categorias_productos.model.CategoryModel;
import com.inventario.demo.entities.categorias_productos.repository.CategoryRepository;
import com.inventario.demo.entities.categorias_productos.service.CategoryService;
import com.inventario.demo.entities.productos.dtoRequest.ProductRequestDto;
import com.inventario.demo.entities.productos.dtoResponse.ProductPageableResponse;
import com.inventario.demo.entities.productos.dtoResponse.ResponseProductRequest;
import com.inventario.demo.entities.productos.mapper.ProductMapper;
import com.inventario.demo.entities.productos.model.ProductModel;
import com.inventario.demo.entities.productos.repository.ProductRepository;
import com.inventario.demo.entities.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.tenant.repository.TenantRepository;
import com.inventario.demo.entities.transaction.dtoRequest.TransactionRequestDto;
import com.inventario.demo.entities.transaction.dtoResponse.TransactionResponseDto;
import com.inventario.demo.entities.transaction.model.TransactionModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final TenantRepository tenantRepository;

    public ResponseProductRequest saveProduct(ProductRequestDto dto){

        CategoryModel categoryModel = categoryRepository.findById(dto.getCategoriaId()).orElseThrow(() -> new CategoryNotFoundException("Categoria no encontrada"));
        TenantModel tenantModel = tenantRepository.findById(dto.getTenantId()).orElseThrow(() -> new TenantNotFoundException( "Tenant no encontrado"));

        ProductModel productModel = productMapper.toEntity(dto);

        productModel.setTenant(tenantModel);
        productModel.setCategoria(categoryModel);

        return productMapper.toDto(productRepository.save(productModel));
    }

    public PaginatedResponse<ResponseProductRequest> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductModel> productsPage = productRepository.findAll(pageable);

        List<ResponseProductRequest> responseDtoAsList = productsPage.getContent().stream()
                .map(productMapper::toDto).collect(Collectors.toList());

        return new PaginatedResponse<>(responseDtoAsList, productsPage.getTotalPages(), productsPage.getTotalElements());

    }

    public ResponseProductRequest getUserById(Long id){
        ProductModel productModel = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));
        
        return productMapper.toDto(productModel);

    }

    public ResponseProductRequest updateProduct(Long id, ProductRequestDto productRequestDto) {
        ProductModel existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product no encontrado con id: " + id));
        productMapper.updateEntityFromDto(productRequestDto, existingProduct);
        ProductModel updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    public void deleteProduct(Long id){
        if(!productRepository.existsById(id)){
            throw new RuntimeException("No se puede eliminar el producto");
        }
        productRepository.deleteById(id);
    }

    public PaginatedResponse<ResponseProductRequest> searchProducts(
            String nombre,
            String sku,
            String configKey,
            String configValue,
            LocalDate startDate,
            LocalDate endDate,
            Long tenantId,
            Long categoryId,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);
        Specification<ProductModel> spec = Specification.where(null);

        // Filtro por nombre (búsqueda insensible a mayúsculas)
        if (nombre != null && !nombre.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }

        // Filtro por SKU (búsqueda insensible a mayúsculas)
        if (sku != null && !sku.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("sku")), "%" + sku.toLowerCase() + "%"));
        }

        // Filtro por rango de fecha de creación
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("creadoEn"), startDate, endDate));
        }

        // Filtro por tenant: se accede al id del tenant
        if (tenantId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("tenant").get("id"), tenantId));
        }

        // Filtro por categoría: se accede al id de la categoría
        if (categoryId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("categoria").get("id"), categoryId));
        }

        // Filtro en el campo JSONB: Se utiliza una función nativa para extraer el valor del JSONB
        if (configKey != null && configValue != null && !configKey.trim().isEmpty() && !configValue.trim().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.function("jsonb_extract_path_text", String.class, root.get("camposPersonalizados"), criteriaBuilder.literal(configKey)), configValue));
        }

        Page<ProductModel> productPage = productRepository.findAll(spec, pageable);
        List<ResponseProductRequest> dtos = productPage.getContent()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(dtos, productPage.getTotalPages(), productPage.getTotalElements());
    }

    public PaginatedResponse<ResponseProductRequest> getAllTenantsByCreadoEnRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductModel> productsPage = productRepository.findAllByCreadoEnBetween(startDate, endDate, pageable);

        List<ResponseProductRequest> productsDtos = productsPage.getContent().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(productsDtos, productsPage.getTotalPages(), productsPage.getTotalElements());
    }

    public ResponseProductRequest updateProductConfiguration(Long id, Map<String, Object> newConfiguration) {
        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product no encontrado con id: " + id));
        product.setCamposPersonalizados(newConfiguration);
        ProductModel updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

}
