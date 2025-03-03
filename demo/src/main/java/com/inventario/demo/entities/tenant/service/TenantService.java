package com.inventario.demo.entities.tenant.service;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.config.exceptions.ResourceNotFoundException;
import com.inventario.demo.config.exceptions.TenantNotFoundException;
import com.inventario.demo.entities.tenant.dtoRequest.TenantRequestDto;
import com.inventario.demo.entities.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.entities.tenant.mapper.TenantMapper;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.tenant.repository.TenantRepository;
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
@Transactional
public class TenantService {
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    public TenantService(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    public PaginatedResponse<TenantResponseDto> getAllTenants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TenantModel> tenantsPage = tenantRepository.findAll(pageable);

        List<TenantResponseDto> tenantDtos = tenantsPage.getContent().stream()
                .map(tenantMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(tenantDtos, tenantsPage.getTotalPages(), tenantsPage.getTotalElements());
    }

    public TenantResponseDto getTenantById(Long id) {
        TenantModel tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant no encontrado con id: " + id));
        return tenantMapper.toDto(tenant);
    }

    public TenantResponseDto createTenant(TenantRequestDto tenantRequestDto) {
        TenantModel tenant = tenantMapper.toEntity(tenantRequestDto);
        TenantModel savedTenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(savedTenant);
    }


    public TenantResponseDto updateTenant(Long id, TenantRequestDto tenantRequestDto) {
        TenantModel existingTenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant no encontrado con id: " + id));
        tenantMapper.updateEntityFromDto(tenantRequestDto, existingTenant);
        TenantModel updatedTenant = tenantRepository.save(existingTenant);
        return tenantMapper.toDto(updatedTenant);
    }


    public void deleteTenant(Long id) {
        TenantModel tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant no encontrado con id: " + id));
        tenantRepository.delete(tenant);
    }

    public PaginatedResponse<TenantResponseDto> searchTenants(
            String name,
            LocalDate startDate,
            LocalDate endDate,
            String configKey,
            String configValue,
            int page,
            int size
    ){
        Pageable pageable = PageRequest.of(page, size);

        Specification<TenantModel> spec = Specification.where(null);

        // Filtro por nombre (búsqueda insensible a mayúsculas/minúsculas)
        if (name != null && !name.trim().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        // Filtro por rango de fechas de creación
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("createdAt"), startDate, endDate));
        }

        // Filtro en el campo JSONB: Se utiliza una función nativa para extraer el valor del JSONB
        if (configKey != null && configValue != null && !configKey.trim().isEmpty() && !configValue.trim().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(criteriaBuilder.function("jsonb_extract_path_text", String.class, root.get("configuration"), criteriaBuilder.literal(configKey)), configValue));
        }

        Page<TenantModel> tenantsPage = tenantRepository.findAll(spec, pageable);

        List<TenantResponseDto> tenantDtos = tenantsPage.getContent().stream()
                .map(tenantMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(tenantDtos, tenantsPage.getTotalPages(), tenantsPage.getTotalElements());
    }

    public PaginatedResponse<TenantResponseDto> getAllTenantsByCreatedAtRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TenantModel> tenantsPage = tenantRepository.findAllByCreatedAtBetween(startDate, endDate, pageable);

        List<TenantResponseDto> tenantDtos = tenantsPage.getContent().stream()
                .map(tenantMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(tenantDtos, tenantsPage.getTotalPages(), tenantsPage.getTotalElements());
    }

    public TenantResponseDto updateTenantConfiguration(Long id, Map<String, Object> newConfiguration) {
        TenantModel tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException("Tenant no encontrado con id: " + id));
        tenant.setConfiguration(newConfiguration);
        TenantModel updatedTenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(updatedTenant);
    }
}
