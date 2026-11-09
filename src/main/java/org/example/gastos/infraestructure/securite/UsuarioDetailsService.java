package org.example.gastos.infraestructure.securite;

import org.example.gastos.infraestructure.entities.UsuarioEntity;
import org.example.gastos.infraestructure.repository.UsuarioJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioDetailsService.class);

    private final UsuarioJpaRepository usuarioJpaRepository;

    public UsuarioDetailsService(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioJpaRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("[AUTH] Usuario no encontrado en BD: '{}'", username);
                    return new UsernameNotFoundException("Usuario no encontrado con username: " + username);
                });

        String rolLimpio = usuario.getRol() != null && usuario.getRol().startsWith("ROLE_")
                ? usuario.getRol().substring(5)
                : (usuario.getRol() != null ? usuario.getRol() : "USER");

        log.info("[AUTH] Usuario cargado de BD: '{}' con rol '{}'", usuario.getUsername(), rolLimpio);

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(rolLimpio)
                .build();
    }
}

