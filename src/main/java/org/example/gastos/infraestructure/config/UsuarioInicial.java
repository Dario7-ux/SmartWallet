package org.example.gastos.infraestructure.config;

import org.example.gastos.infraestructure.entities.UsuarioEntity;
import org.example.gastos.infraestructure.repository.UsuarioJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
public class UsuarioInicial {

    private static final Logger log = LoggerFactory.getLogger(UsuarioInicial.class);

    @Value("${smartwallet.security.default-admin.username:admin}")
    private String adminUsername;

    @Value("${smartwallet.security.default-admin.password:admin123}")
    private String adminPassword;

    @Value("${smartwallet.security.default-admin.role:ADMIN}")
    private String adminRole;

    @Bean
    public CommandLineRunner inicializarUsuarioAdmin(UsuarioJpaRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            sincronizarUsuario(usuarioRepository, passwordEncoder, adminUsername, adminPassword, adminRole);
            sincronizarUsuario(usuarioRepository, passwordEncoder, "usuario", adminPassword, "USER");
        };
    }

    @Transactional
    public void sincronizarUsuario(UsuarioJpaRepository usuarioRepository, PasswordEncoder passwordEncoder,
                                   String username, String rawPassword, String rol) {
        usuarioRepository.findByUsername(username).ifPresentOrElse(
                existente -> {
                    boolean coincide = passwordEncoder.matches(rawPassword, existente.getPassword());
                    if (!coincide) {
                        existente.setPassword(passwordEncoder.encode(rawPassword));
                        existente.setRol(rol);
                        existente.setModificado(LocalDateTime.now());
                        usuarioRepository.saveAndFlush(existente);
                        log.info("[USUARIO INICIAL] Password del usuario '{}' re-encriptado con BCrypt para coincidir con '{}'", username, rawPassword);
                    } else {
                        log.info("[USUARIO INICIAL] Usuario '{}' ya tiene credenciales válidas y activas.", username);
                    }
                },
                () -> {
                    UsuarioEntity nuevo = new UsuarioEntity();
                    nuevo.setUsername(username);
                    nuevo.setPassword(passwordEncoder.encode(rawPassword));
                    nuevo.setRol(rol);
                    nuevo.setCreado(LocalDateTime.now());
                    nuevo.setModificado(LocalDateTime.now());
                    usuarioRepository.saveAndFlush(nuevo);
                    log.info("[USUARIO INICIAL] Usuario '{}' creado exitosamente con rol '{}'", username, rol);
                }
        );
    }
}
