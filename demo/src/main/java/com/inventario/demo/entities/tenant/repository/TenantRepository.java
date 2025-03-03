package com.inventario.demo.entities.tenant.repository;

import com.inventario.demo.entities.productos.model.ProductModel;
import com.inventario.demo.entities.tenant.model.TenantModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TenantRepository extends JpaRepository<TenantModel, Long> {

    Page<TenantModel> findAllByName(String name, Pageable pageable);

    Page<TenantModel> findAllByCreatedAtBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<TenantModel> findAll(Specification<TenantModel> spec, Pageable pageable);

}
