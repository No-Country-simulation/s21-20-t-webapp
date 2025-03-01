package com.inventario.demo.entities.categorias_productos.mapper;

import com.inventario.demo.entities.categorias_productos.dtoRequest.CategoryRequestDto;
import com.inventario.demo.entities.categorias_productos.dtoResponse.ResponseCategoryRequest;
import com.inventario.demo.entities.categorias_productos.model.CategoryModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    private  ModelMapper modelMapper;

    public CategoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoryModel toEntity(CategoryRequestDto categoryRequestDto) {
        return modelMapper.map(categoryRequestDto, CategoryModel.class);
    }

    public ResponseCategoryRequest toDto(CategoryModel categoryModel) {
        return modelMapper.map(categoryModel, ResponseCategoryRequest.class);
    }
}
