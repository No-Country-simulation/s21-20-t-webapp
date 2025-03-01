package com.inventario.demo.inventory.mapper;

import com.inventario.demo.inventory.dtoRequest.InventoryRequestDto;
import com.inventario.demo.inventory.dtoResponse.InventoryResponseDto;
import com.inventario.demo.inventory.model.InventoryModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    private final ModelMapper modelMapper;

    public InventoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public InventoryResponseDto toDto(InventoryModel inventory) {
        return modelMapper.map(inventory, InventoryResponseDto.class);
    }

    public InventoryModel toEntity(InventoryRequestDto dto) {
        return modelMapper.map(dto, InventoryModel.class);
    }

    public void updateEntityFromDto(InventoryRequestDto dto, InventoryModel entity) {
        modelMapper.map(dto, entity);
    }
}
