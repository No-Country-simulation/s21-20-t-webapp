package com.inventario.demo.entities.categorias_productos.repository;

import com.inventario.demo.entities.categorias_productos.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
}
