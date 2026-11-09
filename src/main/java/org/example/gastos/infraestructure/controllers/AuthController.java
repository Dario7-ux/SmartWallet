package org.example.gastos.infraestructure.controllers;

import jakarta.validation.Valid;
import org.example.gastos.infraestructure.controllers.dtos.ActionResponse;
import org.example.gastos.infraestructure.controllers.dtos.LoginRequest;
import org.example.gastos.infraestructure.controllers.dtos.LoginResponse;
import org.example.gastos.infraestructure.entities.UsuarioEntity;
import org.example.gastos.infraestructure.repository.UsuarioJpaRepository;
import org.example.gastos.infraestructure.securite.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JWTService jwtService,
                          UsuarioJpaRepository usuarioJpaRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ActionResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        String username = request.username() != null ? request.username().trim() : "";
        String password = request.password() != null ? request.password().trim() : "";

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UsuarioEntity usuario = usuarioJpaRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generar(usuario.getUsername(), usuario.getRol());
        LoginResponse response = new LoginResponse(token, "Bearer", usuario.getUsername(), usuario.getRol());

        return ResponseEntity.ok(new ActionResponse<>("Autenticación exitosa", response));
    }
}
