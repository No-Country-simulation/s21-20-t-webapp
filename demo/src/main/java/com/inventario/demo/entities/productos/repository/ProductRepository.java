package com.inventario.demo.entities.productos.repository;

import com.inventario.demo.entities.productos.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    Optional<ProductModel> findByNombre(String nombre);

}
