package com.inventario.demo.entities.productos.service;

import com.inventario.demo.entities.productos.dtoRequest.ProductRequestDto;
import com.inventario.demo.entities.productos.dtoResponse.ProductPageableResponse;
import com.inventario.demo.entities.productos.dtoResponse.ResponseProductRequest;
import com.inventario.demo.entities.productos.mapper.ProductMapper;
import com.inventario.demo.entities.productos.model.ProductModel;
import com.inventario.demo.entities.productos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private  ProductMapper productMapper;

    public ProductPageableResponse getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductModel> productsPage = productRepository.findAll(pageable);

        List<ResponseProductRequest> responseDtoAsList = productsPage.getContent().stream()
                .map(productMapper::toDto).collect(Collectors.toList());

        return new ProductPageableResponse(responseDtoAsList, productsPage.getTotalPages(), productsPage.getTotalElements());

    }

    public ResponseProductRequest getUserById(Long id){

        //TODO: Crear Excepcio para este caso
        ProductModel productModel = productRepository.findById(id).orElse(null);
        
        return productMapper.toDto(productModel);

    }


    public ProductModel editProduct(Long id, ProductRequestDto dto){


        ProductModel productModel = productMapper.toEntity(dto);

        //TODO: Crear excepcion para este caso

        return productRepository.findById(id).map(product -> {
            product.setNombre(dto.nombre());
            product.setCategoria(dto.categoria());
            product.setSku(dto.sku());
            product.setCamposPersonalizados(dto.camposPersonalizados());
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
