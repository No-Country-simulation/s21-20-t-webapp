package com.inventario.demo.entities.categorias_productos.service;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.config.exceptions.CategoryNotFoundException;
import com.inventario.demo.config.exceptions.ProductNotFoundException;
import com.inventario.demo.config.exceptions.TenantNotFoundException;
import com.inventario.demo.entities.categorias_productos.dtoRequest.CategoryRequestDto;
import com.inventario.demo.entities.categorias_productos.dtoRequest.CategoryUpdateRequestDto;
import com.inventario.demo.entities.categorias_productos.dtoResponse.ResponseCategoryRequest;
import com.inventario.demo.entities.categorias_productos.dtoResponse.CategoryPageableResponse;
import com.inventario.demo.entities.categorias_productos.mapper.CategoryMapper;
import com.inventario.demo.entities.categorias_productos.model.CategoryModel;
import com.inventario.demo.entities.categorias_productos.repository.CategoryRepository;
import com.inventario.demo.entities.productos.dtoResponse.ResponseProductRequest;
import com.inventario.demo.entities.productos.model.ProductModel;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.tenant.repository.TenantRepository;
import jakarta.validation.Valid;
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

    public ResponseCategoryRequest updateCategory(Long id, CategoryUpdateRequestDto requestDto) {
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

    public PaginatedResponse<ResponseCategoryRequest> searchCategories(
            String nombre,
            String configKey,
            String configValue,
            LocalDate startDate,
            LocalDate endDate,
            Long tenantId,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<CategoryModel> spec = Specification.where(null);

        // Filtro por nombre (búsqueda insensible a mayúsculas)
        if (nombre != null && !nombre.trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }

        // Filtro en el campo JSONB: Se utiliza una función nativa para extraer el valor del JSONB
        if (configKey != null && configValue != null && !configKey.trim().isEmpty() && !configValue.trim().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.function("jsonb_extract_path_text", String.class, root.get("camposPersonalizados"), cb.literal(configKey)), configValue));
        }

        // Filtro por rango de fechas de creación
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("creadoEn"), startDate, endDate));
        }

        // Filtro por tenantId
        if (tenantId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("tenant").get("id"), tenantId));
        }

        Page<CategoryModel> categories = categoryRepository.findAll(spec, pageable);
        List<ResponseCategoryRequest> dtos = categories.getContent().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
        return new PaginatedResponse<>(dtos, categories.getTotalPages(), categories.getTotalElements());

    }

    public PaginatedResponse<ResponseCategoryRequest> getAllCategoriesByCreadoEnRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryModel> categoriesPage = categoryRepository.findAllByCreadoEnBetween(startDate, endDate, pageable);

        List<ResponseCategoryRequest> productsDtos = categoriesPage.getContent().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(productsDtos, categoriesPage.getTotalPages(), categoriesPage.getTotalElements());
    }

    public ResponseCategoryRequest updateCategoryConfiguration(Long id, Map<String, Object> newConfiguration) {
        CategoryModel categoryModel = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria no encontrado con id: " + id));
        categoryModel.setCamposPersonalizados(newConfiguration);
        CategoryModel updatedCategory = categoryRepository.save(categoryModel);
        return categoryMapper.toDto(updatedCategory);
    }
}
