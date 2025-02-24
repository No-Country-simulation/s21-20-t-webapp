package com.inventario.demo.inventory.model;

import com.inventario.demo.tenant.model.TenantModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "inventories")
public class InventoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private ProductModel product;

    @ManyToOne(targetEntity = TenantModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private TenantModel tenant;

    private Integer quantity;

    private String location;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;


}
