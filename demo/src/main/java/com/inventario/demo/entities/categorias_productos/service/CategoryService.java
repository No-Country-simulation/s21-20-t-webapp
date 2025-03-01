package com.inventario.demo.entities.categorias_productos.service;

import com.inventario.demo.entities.categorias_productos.dtoRequest.CategoryRequestDto;
import com.inventario.demo.entities.categorias_productos.dtoResponse.ResponseCategoryRequest;
import com.inventario.demo.entities.categorias_productos.dtoResponse.CategoryPageableResponse;
import com.inventario.demo.entities.categorias_productos.mapper.CategoryMapper;
import com.inventario.demo.entities.categorias_productos.model.CategoryModel;
import com.inventario.demo.entities.categorias_productos.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public ResponseCategoryRequest createCategory(CategoryRequestDto requestDto) {
        CategoryModel category = categoryMapper.toEntity(requestDto);
        CategoryModel savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    public ResponseCategoryRequest getCategoryById(Long id) {
        Optional<CategoryModel> category = categoryRepository.findById(id);
        return category.map(categoryMapper::toDto).orElse(null);
    }

    public CategoryPageableResponse getAllCategories(int page, int size) {
        Page<CategoryModel> categories = categoryRepository.findAll(PageRequest.of(page, size));
        List<ResponseCategoryRequest> dtos = categories.getContent().stream().map(categoryMapper::toDto).toList();
        return new CategoryPageableResponse(dtos, categories.getTotalPages(), categories.getTotalElements());
    }

    public ResponseCategoryRequest updateCategory(Long id, CategoryRequestDto requestDto) {
        if (!categoryRepository.existsById(id)) {
            return null;
        }
        CategoryModel category = categoryMapper.toEntity(requestDto);
        category.setId(id);
        CategoryModel updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    public boolean deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }
}
