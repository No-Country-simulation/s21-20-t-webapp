package com.inventario.demo.entities.productos.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "producto")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Categoria")
    private String categoria;

    @Column(name = "Sku", unique = true)
    private String sku;

    @Column(name = "CamposPersonalizados")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> camposPersonalizados;








}
