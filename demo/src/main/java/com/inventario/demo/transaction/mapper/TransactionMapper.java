package com.inventario.demo.transaction.mapper;

import com.inventario.demo.tenant.model.TenantModel;
import com.inventario.demo.tenant.repository.TenantRepository;
import com.inventario.demo.transaction.dtoRequest.TransactionRequestDto;
import com.inventario.demo.transaction.dtoResponse.TransactionResponseDto;
import com.inventario.demo.transaction.model.TransactionModel;
import com.inventario.demo.user.model.UserModel;
import com.inventario.demo.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

public class TransactionMapper {

    private final ModelMapper modelMapper;

    public TransactionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TransactionResponseDto toDto(TransactionModel transaction) {
        return modelMapper.map(transaction, TransactionResponseDto.class);
    }

    public TransactionModel toEntity(TransactionRequestDto dto) {
        return modelMapper.map(dto, TransactionModel.class);
    }


    public void updateEntityFromDto(TransactionRequestDto dto, TransactionModel entity) {
        modelMapper.map(dto, entity);
    }
}
