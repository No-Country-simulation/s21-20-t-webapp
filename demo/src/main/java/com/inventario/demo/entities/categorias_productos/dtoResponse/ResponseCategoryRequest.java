package com.inventario.demo.entities.categorias_productos.dtoResponse;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
public class ResponseCategoryRequest {

    private Long id;
    private Long tenantId;
    private String Nombre;
    private Map<String, Object> CamposPersonalizados;
    private LocalDate creadoEn;
    private LocalDate actualizadoEn;
}