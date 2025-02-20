package com.inventario.demo.tenant.mapper;

import com.inventario.demo.tenant.dtoRequest.TenantRequestDto;
import com.inventario.demo.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.tenant.model.TenantModel;
import org.modelmapper.ModelMapper;

public class TenantMapper {
    private final ModelMapper modelMapper;

    public TenantMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TenantResponseDto toDto(TenantModel tenant) {
        return modelMapper.map(tenant, TenantResponseDto.class);
    }

    public TenantModel toEntity(TenantRequestDto tenantRequestDto) {
        return modelMapper.map(tenantRequestDto, TenantModel.class);
    }

    public void updateEntityFromDto(TenantRequestDto dto, TenantModel entity) {
        modelMapper.map(dto, entity);
    }
}
