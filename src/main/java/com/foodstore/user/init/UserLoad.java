package com.foodstore.user.init;

import com.foodstore.common.enums.Rol;
import com.foodstore.common.security.PasswordService;
import com.foodstore.user.model.Usuario;
import com.foodstore.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Carga inicial de datos: crea el usuario administrador por defecto
 * si no existe ningún usuario activo en la base de datos.
 *
 * Admin por defecto: admin@admin.com / 123456
 *
 * HU-027: Implementar Carga Inicial de Datos
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoad implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findAll().isEmpty()) {
            log.info("Base de datos vacía — creando usuario administrador por defecto");

            Usuario admin = Usuario.builder()
                    .nombre("Admin")
                    .apellido("Sistema")
                    .email("admin@admin.com")
                    .password(passwordService.encode("123456"))
                    .rol(Rol.ADMIN)
                    .build();

            usuarioRepository.save(admin);
            log.info("Admin creado: admin@admin.com / 123456");
        } else {
            log.info("Usuarios existentes encontrados — omitiendo carga inicial");
        }
    }
}
