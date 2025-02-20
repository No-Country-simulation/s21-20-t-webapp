package com.inventario.demo.tenant.service;

import com.inventario.demo.config.exceptions.ResourceNotFoundException;
import com.inventario.demo.tenant.dtoRequest.TenantRequestDto;
import com.inventario.demo.tenant.dtoResponse.TenantPageResponse;
import com.inventario.demo.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.tenant.mapper.TenantMapper;
import com.inventario.demo.tenant.model.TenantModel;
import com.inventario.demo.tenant.repository.TenantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public TenantPageResponse getAllTenants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TenantModel> tenantsPage = tenantRepository.findAll(pageable);

        List<TenantResponseDto> tenantDtos = tenantsPage.getContent().stream()
                .map(tenantMapper::toDto)
                .collect(Collectors.toList());

        return new TenantPageResponse(tenantDtos, tenantsPage.getTotalPages(), tenantsPage.getTotalElements());
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
}
