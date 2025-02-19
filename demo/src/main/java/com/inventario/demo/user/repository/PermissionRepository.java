package com.inventario.demo.user.repository;

import com.inventario.demo.user.model.PermissionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionModel, Long> {
    Optional<PermissionModel> findByName(String create);
}
