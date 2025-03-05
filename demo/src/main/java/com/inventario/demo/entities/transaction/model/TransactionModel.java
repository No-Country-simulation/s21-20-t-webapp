package com.inventario.demo.entities.transaction.model;

import com.inventario.demo.entities.productos.model.ProductModel;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.user.model.UserModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transactions")
public class TransactionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many transactions belong to one tenant.
    @ManyToOne(targetEntity = TenantModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private TenantModel tenant;

    @ManyToOne(targetEntity = ProductModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "type")
    private String type;

    @Column(name = "reference")
    private String reference;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(targetEntity = UserModel.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private UserModel createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;
}
