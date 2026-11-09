package org.example.gastos.infraestructure.controllers.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PresupuestoRequest(
        @NotNull(message = "El id de categoría es obligatorio")
        Long categoriaId,

        @NotNull(message = "El monto límite es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto límite debe ser mayor a 0")
        BigDecimal montoLimite,

        LocalDate fechaInicio,
        LocalDate fechaFin
) {
}

