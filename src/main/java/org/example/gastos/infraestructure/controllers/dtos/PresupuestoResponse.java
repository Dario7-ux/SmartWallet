package org.example.gastos.infraestructure.controllers.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PresupuestoResponse(
        Long id,
        BigDecimal montoLimite,
        BigDecimal montoActual,
        BigDecimal disponible,
        CategoriaResponse categoria,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        LocalDateTime creado,
        LocalDateTime modificado
) {
}

