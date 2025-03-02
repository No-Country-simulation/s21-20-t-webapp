package com.inventario.demo.entities.categorias_productos.model;

import com.inventario.demo.entities.productos.model.ProductModel;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "categorias_productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = TenantModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private TenantModel tenant;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Type(value = JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> camposPersonalizados = new HashMap<>();

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
