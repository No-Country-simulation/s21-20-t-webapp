package com.inventario.demo.entities.productos.service;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.config.exceptions.CategoryNotFoundException;
import com.inventario.demo.config.exceptions.ProductNotFoundException;
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
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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


    public ProductModel editProduct(Long id, ProductRequestDto dto){


        ProductModel productModel = productMapper.toEntity(dto);

        //TODO: Crear excepcion para este caso

        CategoryModel categoryModel = categoryRepository.findById(dto.getCategoriaId()).orElseThrow(() -> new CategoryNotFoundException("Categoria no encontrado"));
        TenantModel tenantModel = tenantRepository.findById(dto.getTenantId()).orElseThrow(() -> new TenantNotFoundException(( "Tenant no encontrado")));


        return productRepository.findById(id).map(product -> {
            product.setNombre(dto.getNombre());
            product.setTenant(tenantModel);
            product.setCategoria(categoryModel);
            product.setSku(dto.getSku());
            product.setCamposPersonalizados(dto.getCamposPersonalizados());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("No se puede editar el producto"));


    }

    public void deleteProduct(Long id){
        if(!productRepository.existsById(id)){
            throw new RuntimeException("No se puede eliminar el producto");
        }
        productRepository.deleteById(id);
    }

}
