package com.inventario.demo.transaction.model;

import com.inventario.demo.tenant.model.TenantModel;
import com.inventario.demo.user.model.UserModel;
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

    // Many transactions belong to one product.
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Product product;*/

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
