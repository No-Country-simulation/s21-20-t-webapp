package com.inventario.demo.entities.user.service.impl;

import com.inventario.demo.config.jwt.JwtUtils;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.user.dtoRequest.AuthCreateUserRequestDto;
import com.inventario.demo.entities.user.dtoRequest.AuthLoginRequestDto;
import com.inventario.demo.entities.user.dtoResponse.AuthResponseDto;
import com.inventario.demo.entities.user.dtoResponse.AuthResponseRegisterDto;
import com.inventario.demo.entities.user.dtoResponse.UserCreationResult;
import com.inventario.demo.entities.user.model.RoleModel;
import com.inventario.demo.entities.user.model.UserModel;
import com.inventario.demo.entities.user.repository.RoleRepository;
import com.inventario.demo.entities.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                "El usuario con el email " + email + "no existe"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userEntity.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getEnumRole().name())));
        });

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }

    public AuthResponseDto loginUser(@Valid AuthLoginRequestDto authDto) {
        String email = authDto.email();
        String password = authDto.password();

        Long id = userRepository.findByEmail(email)
                .map(UserModel::getId)
                .orElseThrow(() -> new UsernameNotFoundException("El Id del usuario con el correo " + email + " no existe"));


        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtUtils.generateJwtToken(authentication);
        return new AuthResponseDto(id, email, "Usuario logeado exitosamente", token, true);


    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

    }

    public UserCreationResult createUser(@Valid AuthCreateUserRequestDto authCreateUserDto, Long tenantId) {

        String email = authCreateUserDto.email();
        String password = authCreateUserDto.password();
        String username = authCreateUserDto.name();
        String lastName = authCreateUserDto.lastName();
        Long phoneNumber = authCreateUserDto.phoneNumber();
        String country = authCreateUserDto.country();
        LocalDate birthDate = authCreateUserDto.birthDate();

        List<String> roles = authCreateUserDto.roleDto().roles();

        Set<RoleModel> roleEntities = new HashSet<>(roleRepository.findRoleEntitiesByEnumRoleIn(roles));

        if (roleEntities.isEmpty()) {
            throw new IllegalArgumentException("Los roles especificados no existen");
        }

        UserModel userEntity = UserModel.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(username)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .country(country)
                .birthDate(birthDate)
                .tenant(TenantModel.builder().id(tenantId).build())
                .registerDate(LocalDate.now())
                .lastLogin(LocalDate.now())
                .roles(roleEntities)
                .build();

        UserModel userCreated = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();

        userCreated.getRoles().forEach(role ->
                authoritiesList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getEnumRole().name())))
        );

        userCreated.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authoritiesList.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getEmail(), userCreated.getPassword(), authoritiesList);
        String accessToken = jwtUtils.generateJwtToken(authentication);

        // Se crea el DTO que se enviará en el cuerpo sin el token
        AuthResponseRegisterDto responseWithoutToken = new AuthResponseRegisterDto(
                userCreated.getId(),
                username,
                "Usuario registrado exitosamente",
                true
        );

        // Retornamos ambos: la respuesta sin token y el token generado para usar en el header
        return new UserCreationResult(responseWithoutToken, accessToken);
    }

}
