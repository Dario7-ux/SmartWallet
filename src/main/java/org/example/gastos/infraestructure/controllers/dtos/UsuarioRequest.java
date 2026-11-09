package org.example.gastos.infraestructure.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import org.example.gastos.domain.models.Rol;

public record UsuarioRequest(
        @NotBlank(message = "El username es obligatorio")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        Rol rol
) {
}

