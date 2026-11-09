package org.example.gastos.infraestructure.controllers.dtos;

public record LoginResponse(
        String token,
        String tipo,
        String username,
        String rol
) {
}

