package com.inventario.demo.config;

import com.inventario.demo.tenant.model.TenantModel;
import com.inventario.demo.tenant.repository.TenantRepository;
import com.inventario.demo.user.Enum.EnumPermission;
import com.inventario.demo.user.Enum.EnumRole;
import com.inventario.demo.user.model.PermissionModel;
import com.inventario.demo.user.model.RoleModel;
import com.inventario.demo.user.model.UserModel;
import com.inventario.demo.user.repository.PermissionRepository;
import com.inventario.demo.user.repository.RoleRepository;
import com.inventario.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandInitializerConfig  implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TenantRepository tenantRepository;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar permisos
        if (permissionRepository.count() == 0) {
            for (EnumPermission permission : EnumPermission.values()) {
                PermissionModel newPermission = new PermissionModel();
                newPermission.setName(permission.name());
                permissionRepository.save(newPermission);
            }
            System.out.println("Permissions initialized.");
        }

        // Inicializar roles
        if (roleRepository.count() == 0) {
            Set<PermissionModel> allPermissions = new HashSet<>(permissionRepository.findAll());

            RoleModel adminRole = new RoleModel(EnumRole.ADMIN);
            adminRole.setPermissions(allPermissions);
            roleRepository.save(adminRole);

            RoleModel userRole = new RoleModel(EnumRole.USER);
            roleRepository.save(userRole);

            System.out.println("Roles initialized.");
        }

        TenantModel defaultTenant;
        if (tenantRepository.count() == 0) {
            defaultTenant = new TenantModel();
            defaultTenant.setName("Default Tenant");
            defaultTenant.setConfiguration(new HashMap<>()); // o configuración por defecto que requieras
            defaultTenant = tenantRepository.save(defaultTenant);
            System.out.println("Default tenant created.");
        } else {
            // Recuperamos el primer tenant (o el que convenga según tu lógica)
            defaultTenant = tenantRepository.findAll().get(0);
        }

        // Crear usuarios iniciales
        if (userRepository.count() == 0) {
            RoleModel adminRole = roleRepository.findByEnumRole(EnumRole.ADMIN)
                    .orElseThrow(() -> new RuntimeException("El rol ADMIN no existe"));
            RoleModel userRole = roleRepository.findByEnumRole(EnumRole.USER)
                    .orElseThrow(() -> new RuntimeException("El rol USER no existe"));

            UserModel admin = UserModel.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .name("Ryan")
                    .country("Perú")
                    .lastName("Gonzales")
                    .phoneNumber(123456789444L)
                    .birthDate(LocalDate.of(1980, 1, 1))
                    .registerDate(LocalDate.now())
                    .lastLogin(LocalDate.now())
                    .roles(Set.of(adminRole))
                    .tenant(defaultTenant)
                    .build();

            UserModel user1 = UserModel.builder()
                    .email("user1@example.com")
                    .password(passwordEncoder.encode("user123"))
                    .name("Jhon")
                    .country("Argentina")
                    .lastName("Perez")
                    .phoneNumber(123456789L)
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .registerDate(LocalDate.now())
                    .lastLogin(LocalDate.now())
                    .roles(Set.of(userRole))
                    .tenant(defaultTenant)
                    .build();

            UserModel user2 = UserModel.builder()
                    .email("user2@example.com")
                    .password(passwordEncoder.encode("password2"))
                    .name("Rodrigo")
                    .country("Colombia")
                    .lastName("Mendez")
                    .phoneNumber(987654321L)
                    .birthDate(LocalDate.of(1992, 2, 2))
                    .registerDate(LocalDate.now())
                    .lastLogin(LocalDate.now())
                    .roles(Set.of(userRole))
                    .tenant(defaultTenant)
                    .build();



            userRepository.save(admin);
            userRepository.save(user1);
            userRepository.save(user2);

            System.out.println("Initial users created.");
        }
    }
}
