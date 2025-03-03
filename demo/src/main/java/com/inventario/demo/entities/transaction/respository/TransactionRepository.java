package com.inventario.demo.entities.transaction.respository;

import com.inventario.demo.entities.transaction.model.TransactionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
    Page<TransactionModel> findAll(Specification<TransactionModel> spec, Pageable pageable);

    Page<TransactionModel> findAllByCreatedAtBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
