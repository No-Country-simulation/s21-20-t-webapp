package com.inventario.demo.entities.categorias_productos.service;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.config.exceptions.CategoryNotFoundException;
import com.inventario.demo.config.exceptions.TenantNotFoundException;
import com.inventario.demo.entities.categorias_productos.dtoRequest.CategoryRequestDto;
import com.inventario.demo.entities.categorias_productos.dtoResponse.ResponseCategoryRequest;
import com.inventario.demo.entities.categorias_productos.dtoResponse.CategoryPageableResponse;
import com.inventario.demo.entities.categorias_productos.mapper.CategoryMapper;
import com.inventario.demo.entities.categorias_productos.model.CategoryModel;
import com.inventario.demo.entities.categorias_productos.repository.CategoryRepository;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.tenant.repository.TenantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final TenantRepository tenantRepository;

    public ResponseCategoryRequest createCategory(CategoryRequestDto requestDto) {

        TenantModel tenant = tenantRepository.findById(requestDto.getTenantId()).orElseThrow(() -> new TenantNotFoundException("Tenant no encontrado"));
        CategoryModel category = categoryMapper.toEntity(requestDto);
        category.setTenant(tenant);
        CategoryModel savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    public ResponseCategoryRequest getCategoryById(Long id) {
        CategoryModel category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Categoria no encontrada"));
        return categoryMapper.toDto(category);
    }

    public PaginatedResponse<ResponseCategoryRequest> getAllCategories(int page, int size) {
        Page<CategoryModel> categories = categoryRepository.findAll(PageRequest.of(page, size));
        List<ResponseCategoryRequest> dtos = categories.getContent().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
        return new PaginatedResponse<>(dtos, categories.getTotalPages(), categories.getTotalElements());
    }

    public ResponseCategoryRequest updateCategory(Long id, CategoryRequestDto requestDto) {
        CategoryModel category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Categoria no encontrada"));
        categoryMapper.updateEntityFromDto(requestDto, category);
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
