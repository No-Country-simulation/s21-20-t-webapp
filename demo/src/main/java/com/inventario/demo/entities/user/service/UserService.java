package com.inventario.demo.entities.user.service;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.config.exceptions.UserNotFoundException;
import com.inventario.demo.entities.user.dtoRequest.UserRequestDto;
import com.inventario.demo.entities.user.dtoResponse.UserResponseDto;
import com.inventario.demo.entities.user.mapper.UserMapper;
import com.inventario.demo.entities.user.model.UserModel;
import com.inventario.demo.entities.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public PaginatedResponse<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Obtener la página de usuarios
        Page<UserModel> usersPage = userRepository.findAll(pageable);

        // Mapear las entidades a DTOs
        List<UserResponseDto> userDtos = usersPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        // Devolver la respuesta con la información de paginación
        return new PaginatedResponse<>(userDtos, usersPage.getTotalPages(), usersPage.getTotalElements());
    }

    public UserResponseDto getUserById(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado."));

        return userMapper.toDto(user);
    }

    public UserResponseDto updateUser(Long id, @Valid UserRequestDto updatedUserDto) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado."));

        // Actualizamos la entidad usando el mapper dedicado
        userMapper.updateEntityFromDto(updatedUserDto, user);
        UserModel updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado.");
        }
        userRepository.deleteById(id);
    }
}
