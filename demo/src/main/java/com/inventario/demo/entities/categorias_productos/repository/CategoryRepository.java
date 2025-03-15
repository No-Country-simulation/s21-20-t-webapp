package com.inventario.demo.entities.categorias_productos.repository;

import com.inventario.demo.entities.categorias_productos.model.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
    Page<CategoryModel> findAll(Specification<CategoryModel> spec, Pageable pageable);

    Page<CategoryModel> findAllByCreadoEnBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
