package org.example.gastos.infraestructure.controllers.dtos;

import java.time.LocalDateTime;

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        LocalDateTime creado,
        LocalDateTime modificado
) {
}

