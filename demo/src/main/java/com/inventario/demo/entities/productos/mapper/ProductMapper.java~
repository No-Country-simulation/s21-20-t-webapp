package com.inventario.demo.entities.productos.mapper;

import com.inventario.demo.entities.productos.dtoRequest.ProductRequestDto;
import com.inventario.demo.entities.productos.dtoResponse.ResponseProductRequest;
import com.inventario.demo.entities.productos.model.ProductModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProductModel toEntity (ProductRequestDto productRequestDto) {return modelMapper.map(productRequestDto, ProductModel.class);}


    public ResponseProductRequest toDto (ProductModel productModel) {return modelMapper.map(productModel, ResponseProductRequest.class);}
}
