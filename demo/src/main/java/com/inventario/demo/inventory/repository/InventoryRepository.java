package com.inventario.demo.inventory.repository;

import com.inventario.demo.inventory.model.InventoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryModel, Long> {

}
