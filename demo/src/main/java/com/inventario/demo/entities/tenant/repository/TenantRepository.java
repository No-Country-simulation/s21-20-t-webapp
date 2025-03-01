package com.inventario.demo.entities.tenant.repository;

import com.inventario.demo.entities.tenant.model.TenantModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<TenantModel, Long> {

}
