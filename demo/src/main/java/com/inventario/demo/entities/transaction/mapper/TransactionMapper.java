package com.inventario.demo.entities.transaction.mapper;

import com.inventario.demo.entities.transaction.dtoRequest.TransactionRequestDto;
import com.inventario.demo.entities.transaction.dtoResponse.TransactionResponseDto;
import com.inventario.demo.entities.transaction.model.TransactionModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
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
