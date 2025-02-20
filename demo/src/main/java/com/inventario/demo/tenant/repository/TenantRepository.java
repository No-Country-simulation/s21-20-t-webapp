package com.inventario.demo.tenant.repository;

import com.inventario.demo.tenant.model.TenantModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<TenantModel, Long> {

}
