package com.inventario.demo.config;

import com.inventario.demo.entities.user.mapper.UserMapper;
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
}
