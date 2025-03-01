package com.inventario.demo.entities.productos.repository;

import com.inventario.demo.entities.productos.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    Optional<ProductModel> findByNombre(String nombre);

}
