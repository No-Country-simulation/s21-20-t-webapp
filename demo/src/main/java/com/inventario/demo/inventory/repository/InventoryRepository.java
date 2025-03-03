package com.inventario.demo.inventory.repository;

import com.inventario.demo.entities.transaction.model.TransactionModel;
import com.inventario.demo.inventory.model.InventoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<InventoryModel, Long> {

    Page<InventoryModel> findAll(Specification<InventoryModel> spec, Pageable pageable);

    Page<InventoryModel> findAllByCreatedAtBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
