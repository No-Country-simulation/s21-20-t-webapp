package com.inventario.demo.config;

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
        mapper.typeMap(TransactionRequestDto.class, TransactionModel.class)
                .addMappings(m -> {
                    m.skip(TransactionModel::setId);
                    m.skip(TransactionModel::setTenant);
                    m.skip(TransactionModel::setCreatedBy);
                });

        mapper.typeMap(InventoryRequestDto.class, InventoryModel.class)
                .addMappings(m -> {
                    m.skip(InventoryModel::setId);
                    //m.skip((InventoryModel::setProduct));
                    m.skip(InventoryModel::setTenant);
                });
        return mapper;
    }
}
