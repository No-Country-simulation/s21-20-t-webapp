package com.inventario.demo.user.mapper;

import com.inventario.demo.user.dtoRequest.UserRequestDto;
import com.inventario.demo.user.dtoResponse.UserResponseDto;
import com.inventario.demo.user.model.UserModel;
import org.modelmapper.ModelMapper;

public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Mapea de DTO a entidad
    public UserModel toEntity(UserRequestDto dto) {
        return modelMapper.map(dto, UserModel.class);
    }

    // Mapea de entidad a DTO
    public UserResponseDto toDto(UserModel user) {
        return modelMapper.map(user, UserResponseDto.class);
    }

    public void updateEntityFromDto(UserRequestDto dto, UserModel entity) {
        modelMapper.map(dto, entity);
    }

}
