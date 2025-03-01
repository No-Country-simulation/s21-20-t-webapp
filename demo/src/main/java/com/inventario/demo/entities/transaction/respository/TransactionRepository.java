package com.inventario.demo.entities.transaction.respository;

import com.inventario.demo.entities.transaction.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
}
