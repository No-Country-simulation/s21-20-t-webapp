package com.inventario.demo.transaction.respository;

import com.inventario.demo.transaction.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {
}
