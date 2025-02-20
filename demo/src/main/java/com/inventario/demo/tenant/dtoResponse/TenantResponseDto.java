package com.inventario.demo.tenant.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TenantResponseDto {
    private Long id;
    private String name;
    private Map<String, Object> configuration;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
