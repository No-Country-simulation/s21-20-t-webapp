package com.inventario.demo.entities.user.repository;

import com.inventario.demo.entities.user.model.PermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionModel, Long> {
    Optional<PermissionModel> findByName(String create);
}
