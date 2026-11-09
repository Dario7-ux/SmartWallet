package org.example.gastos.infraestructure.controllers.dtos;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String username,
        String rol,
        boolean activo,
        LocalDateTime creado,
        LocalDateTime modificado
) {
}

