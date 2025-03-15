package com.inventario.demo.entities.productos.model;

import com.inventario.demo.entities.categorias_productos.model.CategoryModel;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String sku;

    @Type(value = JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> camposPersonalizados = new HashMap<>();

    @ManyToOne(targetEntity = TenantModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private TenantModel tenant;

    @ManyToOne(targetEntity = CategoryModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoryModel categoria;

    @Column(nullable = false, updatable = false)
    private LocalDate creadoEn;

    @Column(nullable = false)
    private LocalDate actualizadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = LocalDate.now();
        this.actualizadoEn = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEn = LocalDate.now();
    }
}
