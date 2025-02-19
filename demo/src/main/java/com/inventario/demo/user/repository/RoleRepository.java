package com.inventario.demo.user.repository;

import com.inventario.demo.user.Enum.EnumRole;
import com.inventario.demo.user.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    List<RoleModel> findRoleEntitiesByEnumRoleIn(List<String> roles);
    Optional<RoleModel> findByEnumRole(EnumRole role);
}
