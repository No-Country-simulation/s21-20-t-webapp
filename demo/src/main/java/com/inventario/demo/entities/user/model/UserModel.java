package com.inventario.demo.entities.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private Long phoneNumber;

    private String country;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "register_date")
    private LocalDate registerDate = LocalDate.now();

    @Column(name = "last_login")
    private LocalDate lastLogin = LocalDate.now();

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleModel.class)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roles = new HashSet<>();

}
