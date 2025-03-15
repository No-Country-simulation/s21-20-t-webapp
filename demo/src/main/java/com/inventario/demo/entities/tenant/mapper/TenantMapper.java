package com.inventario.demo.entities.tenant.mapper;

import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.tenant.dtoRequest.TenantRequestDto;
import com.inventario.demo.entities.tenant.dtoResponse.TenantResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
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
