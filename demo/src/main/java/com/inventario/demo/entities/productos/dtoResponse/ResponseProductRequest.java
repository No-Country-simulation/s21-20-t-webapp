package com.inventario.demo.entities.productos.dtoResponse;

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
public class ResponseProductRequest{
    private Long id;
    private Long tenantId;
    private Long categoriaId;
    private String nombre;
    private String sku;
    private Map<String, Object> camposPersonalizados;
    private LocalDate creadoEn;
    private LocalDate actualizadoEn;
}
