package com.inventario.demo.config;

import com.inventario.demo.tenant.mapper.TenantMapper;
import com.inventario.demo.user.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UserMapper userMapper(ModelMapper modelMapper) {
        return new UserMapper(modelMapper);
    }

    @Bean
    public TenantMapper tenantMapper(ModelMapper modelMapper) {
        return new TenantMapper(modelMapper);
    }
}
