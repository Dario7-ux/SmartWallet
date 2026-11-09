package org.example.gastos.infraestructure.controllers.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GastoRequest(
        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal monto,

        String moneda,

        LocalDateTime fecha,

        @NotNull(message = "El id de categoría es obligatorio")
        Long categoriaId
) {
}

