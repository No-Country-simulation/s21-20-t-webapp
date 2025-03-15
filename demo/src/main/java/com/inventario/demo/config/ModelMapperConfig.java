package com.inventario.demo.config;

import com.inventario.demo.entities.categorias_productos.dtoRequest.CategoryRequestDto;
import com.inventario.demo.entities.categorias_productos.model.CategoryModel;
import com.inventario.demo.entities.productos.dtoRequest.ProductRequestDto;
import com.inventario.demo.entities.productos.model.ProductModel;
import com.inventario.demo.entities.transaction.dtoRequest.TransactionRequestDto;
import com.inventario.demo.entities.transaction.model.TransactionModel;
import com.inventario.demo.inventory.dtoRequest.InventoryRequestDto;
import com.inventario.demo.inventory.model.InventoryModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
        mapper.typeMap(TransactionRequestDto.class, TransactionModel.class)
                .addMappings(m -> {
                    m.skip(TransactionModel::setId);
                    m.skip(TransactionModel::setTenant);
                    m.skip(TransactionModel::setCreatedBy);
                    m.skip(TransactionModel::setProduct);
                });

        mapper.typeMap(InventoryRequestDto.class, InventoryModel.class)
                .addMappings(m -> {
                    m.skip(InventoryModel::setId);
                    m.skip((InventoryModel::setProduct));
                    m.skip(InventoryModel::setTenant);
                });

        mapper.typeMap(CategoryRequestDto.class, CategoryModel.class)
                .addMappings(m -> {
                    m.skip(CategoryModel::setId);
                    m.skip(CategoryModel::setTenant);
                });

        mapper.typeMap(ProductRequestDto.class, ProductModel.class)
                .addMappings(m -> {
                    m.skip(ProductModel::setId);
                    m.skip(ProductModel::setTenant);
                    m.skip(ProductModel::setCategoria);
                });
        return mapper;
    }
}
