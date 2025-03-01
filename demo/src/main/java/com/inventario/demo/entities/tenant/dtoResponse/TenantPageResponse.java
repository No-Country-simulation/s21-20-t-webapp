package com.inventario.demo.entities.tenant.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantPageResponse {
    private List<TenantResponseDto> tenants;
    private int totalPages;
    private long totalElements;
}
