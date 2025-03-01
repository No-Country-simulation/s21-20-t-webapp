package com.inventario.demo.config;

import com.inventario.demo.entities.tenant.mapper.TenantMapper;
import com.inventario.demo.entities.transaction.dtoRequest.TransactionRequestDto;
import com.inventario.demo.entities.transaction.mapper.TransactionMapper;
import com.inventario.demo.entities.transaction.model.TransactionModel;
import com.inventario.demo.entities.user.mapper.UserMapper;
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
        return mapper;
    }

    @Bean
    public UserMapper userMapper(ModelMapper modelMapper) {
        return new UserMapper(modelMapper);
    }

    @Bean
    public TenantMapper tenantMapper(ModelMapper modelMapper) {
        return new TenantMapper(modelMapper);
    }

    @Bean
    public TransactionMapper transactionMapper(ModelMapper modelMapper) {

        return new TransactionMapper(modelMapper);
    }

}
