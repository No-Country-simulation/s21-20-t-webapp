package com.inventario.demo.user.service;

import com.inventario.demo.config.exceptions.UserNotFoundException;
import com.inventario.demo.user.dtoRequest.UserRequestDto;
import com.inventario.demo.user.dtoResponse.UserPageResponse;
import com.inventario.demo.user.dtoResponse.UserResponseDto;
import com.inventario.demo.user.mapper.UserMapper;
import com.inventario.demo.user.model.UserModel;
import com.inventario.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserPageResponse getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Obtener la página de usuarios
        Page<UserModel> usersPage = userRepository.findAll(pageable);

        // Mapear las entidades a DTOs
        List<UserResponseDto> userDtos = usersPage.getContent().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        // Devolver la respuesta con la información de paginación
        return new UserPageResponse(userDtos, usersPage.getTotalPages(), usersPage.getTotalElements());
    }

    public UserResponseDto getUserById(Long id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado."));

        return userMapper.toDto(user);
    }

    public UserModel updateUser(Long id, UserRequestDto updatedUserDto) {
        UserModel updatedUser = userMapper.toEntity(updatedUserDto);
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setLastName(updatedUser.getLastName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPhoneNumber(updatedUser.getPhoneNumber());
                    user.setBirthDate(updatedUser.getBirthDate());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + id + " no encontrado."));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario con ID " + id + " no encontrado.");
        }
        userRepository.deleteById(id);
    }
}
