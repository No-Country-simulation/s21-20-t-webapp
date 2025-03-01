package com.inventario.demo.inventory.service;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.config.exceptions.InventoryNotFoundException;
import com.inventario.demo.config.exceptions.TenantNotFoundException;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.tenant.repository.TenantRepository;
import com.inventario.demo.inventory.dtoRequest.InventoryRequestDto;
import com.inventario.demo.inventory.dtoResponse.InventoryResponseDto;
import com.inventario.demo.inventory.mapper.InventoryMapper;
import com.inventario.demo.inventory.model.InventoryModel;
import com.inventario.demo.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final TenantRepository tenantRepository;

    public PaginatedResponse<InventoryResponseDto> getAllInventories(int page, int size) {
        Page<InventoryModel> inventoryPage = inventoryRepository.findAll(PageRequest.of(page, size));

        List<InventoryResponseDto> inventoryDtos = inventoryPage.getContent().stream()
                .map(inventoryMapper::toDto)
                .collect(Collectors.toList());
        return new PaginatedResponse<>(inventoryDtos, inventoryPage.getTotalPages(), inventoryPage.getTotalElements());
    }

    public InventoryResponseDto getInventoryById(Long id) {
        InventoryModel inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("El inventario no fue encontrado con id: " + id));
        return inventoryMapper.toDto(inventory);
    }

    public InventoryResponseDto createInventory(InventoryRequestDto inventoryRequestDto) {
        TenantModel tenant = tenantRepository.findById(inventoryRequestDto.getTenantId())
                .orElseThrow(() -> new TenantNotFoundException("Tenant no encontrado"));

        InventoryModel inventory = inventoryMapper.toEntity(inventoryRequestDto);

        inventory.setTenant(tenant);

        InventoryModel savedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.toDto(savedInventory);
    }

    public InventoryResponseDto updateInventory(Long id, InventoryRequestDto inventoryRequestDto) {
        InventoryModel existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("El inventario no fue encontrado con id: " + id));
        inventoryMapper.updateEntityFromDto(inventoryRequestDto, existingInventory);
        InventoryModel updatedInventory = inventoryRepository.save(existingInventory);
        return inventoryMapper.toDto(updatedInventory);
    }

    public void deleteInventory(Long id) {
        InventoryModel inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("El inventario no fue encontrado con id: " + id));
        inventoryRepository.delete(inventory);
    }

}
