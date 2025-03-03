package com.inventario.demo.entities.productos.repository;

import com.inventario.demo.entities.productos.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    Optional<ProductModel> findByNombre(String nombre);

    Page<ProductModel> findAll(Specification<ProductModel> spec, Pageable pageable);

    Page<ProductModel> findAllByCreadoEnBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
